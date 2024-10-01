package social.firefly.feature.report.step3

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.reportScreen3() {
    composable<NavigationDestination.ReportScreen3> { backStackEntry ->
        val reportAccountId: String =
            backStackEntry.toRoute<NavigationDestination.ReportScreen3>().reportAccountId
        val reportAccountHandle: String =
            backStackEntry.toRoute<NavigationDestination.ReportScreen3>().reportAccountHandle
        val didUserReportAccount: Boolean =
            backStackEntry.toRoute<NavigationDestination.ReportScreen3>().didUserReportAccount

        ReportScreen3(
            reportAccountId = reportAccountId,
            reportAccountHandle = reportAccountHandle,
            didUserReportAccount = didUserReportAccount,
        )
    }
}
