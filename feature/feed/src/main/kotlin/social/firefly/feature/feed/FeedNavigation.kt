package social.firefly.feature.feed

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.BottomBarNavigationDestination

fun NavGraphBuilder.feedScreen() {
    composable<BottomBarNavigationDestination.Feed> {
        FeedScreen()
    }
}
