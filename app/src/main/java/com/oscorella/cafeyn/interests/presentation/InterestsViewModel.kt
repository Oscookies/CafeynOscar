package com.oscorella.cafeyn.interests.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscorella.cafeyn.interests.data.TopicRepository
import com.oscorella.cafeyn.interests.domain.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel
@Inject constructor(
    private val topicRepository: TopicRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<InterestsUiState>(InterestsUiState.Loading)
    val uiState = _uiState
        .onStart {
            getTopics()
            getFavoriteTopics()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InterestsUiState.Loading,
        )

    private val _favoritesList: MutableStateFlow<List<Topic>> = MutableStateFlow(emptyList())
    val favoritesList: StateFlow<List<Topic>> = _favoritesList.asStateFlow()

    private val _topicList: MutableStateFlow<List<Topic>> = MutableStateFlow(emptyList())
    val topicList: StateFlow<List<Topic>> = _topicList.asStateFlow()

    private fun getTopics() {
        viewModelScope.launch {
            topicRepository.getAllTopics(
                onSuccess = {
                    _topicList.tryEmit(it)
                    _uiState.tryEmit(InterestsUiState.Idle)
                },
                onError = {
                    _uiState.tryEmit(InterestsUiState.Error(it))
                },
            )
        }
    }

    private fun getFavoriteTopics() {
        viewModelScope.launch {
            _favoritesList.emit(
                topicRepository.getFavoriteTopics()
            )
        }
    }

    fun addToFavorites(topic: Topic, index: Int) {
        _topicList.tryEmit(_topicList.value.toMutableList().apply { remove(topic) })
        topic.index = index
        _favoritesList.tryEmit(_favoritesList.value.toMutableList().apply { add(topic) })
    }

    fun deleteFavorite(topic: Topic) {
        _favoritesList.tryEmit(_favoritesList.value.toMutableList().apply { remove(topic) })
        _topicList.tryEmit(_topicList.value.toMutableList().apply { add(topic.index, topic) })
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