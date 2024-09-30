package com.oscorella.cafeyn.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.oscorella.cafeyn.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    onNavigateToInterests: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.cafeyn_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
        )
        Button(
            onClick = {
                onNavigateToInterests()
            },
            Modifier
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.interests),
                modifier = Modifier
                    .padding(8.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = "text"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }
                    )
            )
        }

    }

}