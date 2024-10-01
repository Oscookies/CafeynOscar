package com.oscorella.cafeyn.interests.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscorella.cafeyn.core.network.Result
import com.oscorella.cafeyn.interests.data.TopicRepository
import com.oscorella.cafeyn.interests.domain.Topic
import com.oscorella.cafeyn.interests.domain.TopicComparator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.TreeSet
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel
@Inject constructor(
    private val topicRepository: TopicRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<InterestsUiState>(InterestsUiState.Loading)
    // Publicly exposed ui state that triggers the fetching of topics when collexcted
    val uiState = _uiState
        .onStart {
            getTopics()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InterestsUiState.Loading,
        )

    // Holds the current list of favorite topics
    private val _favoritesList: MutableStateFlow<List<Topic>> = MutableStateFlow(emptyList())
    val favoritesList: StateFlow<List<Topic>> = _favoritesList.asStateFlow()

    // Holds the list of all available topics, excluding favorites
    private val _topicList: MutableStateFlow<List<Topic>> = MutableStateFlow(emptyList())
    val topicList: StateFlow<List<Topic>> = _topicList.asStateFlow()

    // TreeSet to maintain ordered topics
    private val orderedTopics: TreeSet<Topic> = TreeSet(TopicComparator())

    fun getTopics() {
        viewModelScope.launch {
            when (val topics = topicRepository.getAllTopics()) {
                is Result.Success -> {
                    handleSuccessResult(topics.data)
                }
                is Result.Error -> {
                    _uiState.tryEmit(InterestsUiState.Error(topics.error.plus(topics.errorCode)))
                }
            }
        }
    }

    suspend fun handleSuccessResult(topics: List<Topic>) {
        val favoriteTopics = topicRepository.getFavoriteTopics()
        orderedTopics.addAll(topics)
        orderedTopics.removeAll(favoriteTopics.toSet())
        _favoritesList.tryEmit(favoriteTopics)
        _topicList.tryEmit(orderedTopics.toList())
        _uiState.tryEmit(InterestsUiState.Idle)
    }

    fun addToFavorites(topic: Topic, index: Int) {
        orderedTopics.remove(topic)
        _topicList.tryEmit(orderedTopics.toList())
        _favoritesList.tryEmit(_favoritesList.value.toMutableList().apply { add(topic) })
    }

    fun deleteFavorite(topic: Topic) {
        _favoritesList.tryEmit(_favoritesList.value.toMutableList().apply { remove(topic) })
        orderedTopics.add(topic)
        _topicList.tryEmit(orderedTopics.toList())
    }

    fun saveFavorites() {
        viewModelScope.launch {
            topicRepository.saveFavoriteTopics(_favoritesList.value)
            _uiState.emit(InterestsUiState.FavoritesSaved)
        }
    }
}

sealed interface InterestsUiState {

    data object Idle: InterestsUiState

    data object Loading: InterestsUiState

    data object FavoritesSaved: InterestsUiState

    data class Error(val message: String?): InterestsUiState

}