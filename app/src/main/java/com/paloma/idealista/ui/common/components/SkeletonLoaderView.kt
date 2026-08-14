package com.paloma.idealista.ui.common.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    val alpha by shimmerAlpha()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, SkeletonGray, RoundedCornerShape(12.dp))
    ) {
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cornerRadius = 0.dp,
            alpha = alpha
        )
        Column(modifier = Modifier.padding(16.dp)) {
            SkeletonBlock(
                modifier = Modifier
                    .width(140.dp)
                    .height(22.dp),
                alpha = alpha
            )
            Spacer(modifier = Modifier.height(8.dp))
            SkeletonBlock(
                modifier = Modifier
                    .width(180.dp)
                    .height(14.dp),
                alpha = alpha
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                SkeletonBlock(modifier = Modifier.width(28.dp).height(14.dp), alpha = alpha)
                Spacer(modifier = Modifier.width(12.dp))
                SkeletonBlock(modifier = Modifier.width(28.dp).height(14.dp), alpha = alpha)
                Spacer(modifier = Modifier.width(12.dp))
                SkeletonBlock(modifier = Modifier.width(50.dp).height(14.dp), alpha = alpha)
            }
        }
    }
}

@Composable
private fun SkeletonBlock(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 4.dp,
    alpha: Float
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(SkeletonGray.copy(alpha = alpha))
    ) {}
}

@Composable
private fun shimmerAlpha(): State<Float> {
    val transition = rememberInfiniteTransition(label = "shimmer")
    return transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )
}

@Composable
fun AdDetailSkeleton() {
    val alpha by shimmerAlpha()

    Column(modifier = Modifier.padding(0.dp)) {
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            cornerRadius = 0.dp,
            alpha = alpha
        )
        Column(modifier = Modifier.padding(16.dp)) {
            SkeletonBlock(modifier = Modifier.width(180.dp).height(26.dp), alpha = alpha)
            Spacer(modifier = Modifier.height(12.dp))
            SkeletonBlock(modifier = Modifier.width(240.dp).height(16.dp), alpha = alpha)
            Spacer(modifier = Modifier.height(20.dp))
            SkeletonBlock(modifier = Modifier.width(100.dp).height(18.dp), alpha = alpha)
            Spacer(modifier = Modifier.height(8.dp))
            SkeletonBlock(modifier = Modifier.fillMaxWidth().height(14.dp), alpha = alpha)
            Spacer(modifier = Modifier.height(6.dp))
            SkeletonBlock(modifier = Modifier.fillMaxWidth().height(14.dp), alpha = alpha)
            Spacer(modifier = Modifier.height(6.dp))
            SkeletonBlock(modifier = Modifier.width(200.dp).height(14.dp), alpha = alpha)
        }
    }
}

private val SkeletonGray = Color(0xFFE8E8E8)