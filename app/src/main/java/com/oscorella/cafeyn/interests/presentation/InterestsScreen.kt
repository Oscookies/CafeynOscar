@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalSharedTransitionApi::class)

package com.oscorella.cafeyn.interests.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oscorella.cafeyn.R
import com.oscorella.cafeyn.interests.domain.Topic

@Composable
fun SharedTransitionScope.InterestsScreen(
    onNavigateBack: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    interestsViewModel: InterestsViewModel = hiltViewModel(),
) {

    val uiState by interestsViewModel.uiState.collectAsStateWithLifecycle()
    val topics by interestsViewModel.topicList.collectAsStateWithLifecycle()
    val favoriteTopics by interestsViewModel.favoritesList.collectAsStateWithLifecycle()

    InterestsContent(
        onNavigateBack,
        animatedVisibilityScope,
        uiState,
        topics,
        favoriteTopics,
        interestsViewModel::addToFavorites,
        interestsViewModel::deleteFavorite,
        interestsViewModel::saveFavorites,
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SharedTransitionScope.InterestsContent(
    onNavigateBack: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    uiState: InterestsUiState,
    topics: List<Topic>,
    favoriteTopics: List<Topic>,
    addToFavorites: (Topic, Int) -> Unit,
    deleteFromFavorites: (Topic) -> Unit,
    saveFavorites: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        when(uiState) {
            is InterestsUiState.FavoritesSaved -> {
                LaunchedEffect(key1 = uiState) {
                    onNavigateBack()
                }

            }
            is InterestsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is InterestsUiState.Error -> {
                Text(
                    text = "Error: ".plus(uiState.message),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .sharedElement(
                            state = rememberSharedContentState(key = "text"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 1000)
                            }
                        )
                )
            }
            InterestsUiState.Idle -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.quit),
                        modifier = Modifier.clickable {
                            onNavigateBack()
                        }
                    )
                    Text(
                        text = stringResource(R.string.interests),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = "text"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 500)
                                }
                            )
                    )
                    Text(
                        text = stringResource(R.string.save),
                        modifier = Modifier
                            .clickable {
                                saveFavorites()
                            }
                    )
                }
                Text(
                    text = stringResource(R.string.interest_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(R.string.interest_subtitle),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if(favoriteTopics.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .padding(12.dp),
                            text = stringResource(R.string.no_topic_selected_title),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .padding(12.dp),
                            text = stringResource(R.string.no_topic_selected_subtitle),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else {
                    LazyColumn(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        itemsIndexed(
                            items = favoriteTopics,
                            key = { _, topic -> topic.id }
                        ) { index, topic ->
                            TopicItem(
                                topic,
                                index,
                                true,
                                addToFavorites,
                                deleteFromFavorites,
                                modifier = Modifier
                                    .animateItem()
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(R.string.discover),
                    style = MaterialTheme.typography.titleMedium,
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(
                        items = topics,
                        key = { _, topic -> topic.id }
                    ) { index, topic ->
                        TopicItem(
                            topic,
                            index,
                            false,
                            addToFavorites,
                            deleteFromFavorites,
                            modifier = Modifier
                                .animateItem()
                        )
                    }
                }
            }
        }

    }
    
}

@Composable
fun TopicItem(
    topic: Topic,
    index: Int,
    isFav: Boolean,
    addToFavorites: (Topic, Int) -> Unit,
    deleteFavorite: (Topic) -> Unit,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable {
                if (isFav) {
                    deleteFavorite(topic)
                } else {
                    addToFavorites(topic, index)
                }
            }
    ) {
        Image(
            painter = if(isFav) painterResource(id = R.drawable.minus_icon) else painterResource(id = R.drawable.plus_icon),
            modifier = Modifier.size(24.dp),
            contentDescription = null
        )
        Text(
            text = topic.name.raw,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview
@Composable
fun InterestsScreenPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            Surface {
                InterestsScreen({},animatedVisibilityScope = this)
            }
        }
    }
}