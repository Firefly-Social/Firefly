package social.firefly.feature.thread

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.threadScreen() {
    composable<NavigationDestination.Thread> { backStackEntry ->
        val threadStatusId: String =
            backStackEntry.toRoute<NavigationDestination.Thread>().threadStatusId

        ThreadScreen(
            threadStatusId = threadStatusId,
        )
    }
}
