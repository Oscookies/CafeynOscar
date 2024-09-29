@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.oscorella.cafeyn.interests

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oscorella.cafeyn.R

@Composable
fun SharedTransitionScope.InterestsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.quit)
            )
            Text(
                text = stringResource(R.string.interests),
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
            Text(
                text = stringResource(R.string.save)
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
        LazyColumn() {

        }
        Text(
            text = stringResource(R.string.discover),
            style = MaterialTheme.typography.titleMedium,
        )
        LazyColumn() {

        }

    }
}

@Preview
@Composable
fun InterestsScreenPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            Surface {
                InterestsScreen(animatedVisibilityScope = this)
            }
        }
    }
}