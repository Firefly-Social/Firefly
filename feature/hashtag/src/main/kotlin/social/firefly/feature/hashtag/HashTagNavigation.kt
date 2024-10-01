package social.firefly.feature.hashtag

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.hashTagScreen() {
    composable<NavigationDestination.HashTag> { backStackEntry ->
        val hashTagValue: String = backStackEntry.toRoute<NavigationDestination.HashTag>().hashtag

        HashTagScreen(
            hashTag = hashTagValue,
        )
    }
}
