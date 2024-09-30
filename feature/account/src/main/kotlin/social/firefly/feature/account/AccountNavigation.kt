package social.firefly.feature.account

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.accountScreen() {
    composable<NavigationDestination.Account> { backStackEntry ->
        val accountId: String = backStackEntry.toRoute<NavigationDestination.Account>().accountId
        AccountScreen(
            accountId = accountId,
        )
    }
}
