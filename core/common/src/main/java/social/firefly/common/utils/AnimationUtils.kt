package social.firefly.common.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

fun AnimatedContentTransitionScope<NavBackStackEntry>.FfSlideIn(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(TWEEN_DURATION),
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.FfSlideOut(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(TWEEN_DURATION),
    )

fun FfFadeIn(): EnterTransition = fadeIn(tween(durationMillis = TWEEN_DURATION))

fun FfFadeOut(): ExitTransition = fadeOut(tween(durationMillis = TWEEN_DURATION))

const val TWEEN_DURATION = 250
