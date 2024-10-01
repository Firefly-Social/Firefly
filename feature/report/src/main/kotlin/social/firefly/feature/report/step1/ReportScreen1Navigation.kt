package social.firefly.feature.report.step1

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.reportScreen1() {
    composable<NavigationDestination.ReportScreen1> { backStackEntry ->

        val reportAccountId: String =
            backStackEntry.toRoute<NavigationDestination.ReportScreen1>().reportAccountId
        val reportAccountHandle: String =
            backStackEntry.toRoute<NavigationDestination.ReportScreen1>().reportAccountHandle
        val reportStatusId: String? =
            backStackEntry.toRoute<NavigationDestination.ReportScreen1>().reportStatusId

        ReportScreen1(
            reportAccountId = reportAccountId,
            reportAccountHandle = reportAccountHandle,
            reportStatusId = reportStatusId,
        )
    }
}
