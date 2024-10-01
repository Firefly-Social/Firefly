package social.firefly.feature.media

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.mediaScreen() {
    composable<NavigationDestination.Media> { backStackEntry ->
        val statusId: String = backStackEntry.toRoute<NavigationDestination.Media>().statusId
        val startIndex: Int = backStackEntry.toRoute<NavigationDestination.Media>().startIndex

        MediaScreen(
            statusId = statusId,
            startIndex = startIndex,
        )
    }
}
