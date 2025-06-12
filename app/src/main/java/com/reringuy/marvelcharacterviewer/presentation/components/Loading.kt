package com.reringuy.marvelcharacterviewer.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.scale

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    Box(modifier) {
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .background(color = backgroundColor)
        ) {
            Row(
                Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircleLoading(
                    modifier = Modifier.size(84.dp),
                    circleColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun CircleLoading(
    modifier: Modifier,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    animationDelay: Int = 1000,
) {
    var circleScale by remember {
        mutableFloatStateOf(0f)
    }

    val circleScaleAnimate = animateFloatAsState(
        targetValue = circleScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDelay
            )
        ), label = ""
    )

    LaunchedEffect(Unit) {
        circleScale = 1f
    }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale = circleScaleAnimate.value)
                .border(
                    width = 4.dp,
                    color = circleColor.copy(alpha = 1 - circleScaleAnimate.value),
                    shape = CircleShape
                )
        ) {

        }
    }
}