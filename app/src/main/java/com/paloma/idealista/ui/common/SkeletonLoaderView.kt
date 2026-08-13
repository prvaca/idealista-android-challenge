package com.paloma.idealista.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AdSkeletonList(itemCount: Int = 3) {
    Column {
        repeat(itemCount) {
            AdSkeletonCard()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AdSkeletonCard() {
    val shimmerAlpha by shimmerAlpha()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            SkeletonBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                cornerRadius = 0.dp,
                alpha = shimmerAlpha
            )
            SkeletonBlock(
                modifier = Modifier
                    .width(140.dp)
                    .height(20.dp)
                    .padding(16.dp),
                alpha = shimmerAlpha
            )
            SkeletonBlock(
                modifier = Modifier
                    .width(200.dp)
                    .height(14.dp)
                    .padding(start = 16.dp, bottom = 16.dp),
                alpha = shimmerAlpha
            )
        }
    }
}

@Composable
private fun SkeletonBlock(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 6.dp,
    alpha: Float
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
    ) {}
}

@Composable
private fun shimmerAlpha(): State<Float> {
    val transition = rememberInfiniteTransition(label = "shimmer")
    return transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )
}