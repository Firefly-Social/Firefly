package social.firefly.feature.followers

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.followersScreen() {
    composable<NavigationDestination.Followers> { backStackEntry ->

        val accountId: String = backStackEntry.toRoute<NavigationDestination.Followers>().accountId
        val displayName: String = backStackEntry.toRoute<NavigationDestination.Followers>().displayName
        val startingTab: NavigationDestination.Followers.StartingTab =
            backStackEntry.toRoute<NavigationDestination.Followers>().startingTab

        FollowersScreen(
            accountId = accountId,
            displayName = displayName,
            startingTab = when (startingTab) {
                NavigationDestination.Followers.StartingTab.FOLLOWERS -> FollowType.FOLLOWERS
                else -> FollowType.FOLLOWING
            }
        )
    }
}
