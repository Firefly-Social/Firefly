package social.firefly.feature.report.step2

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.json.Json
import social.firefly.core.model.InstanceRule
import social.firefly.core.model.ReportType
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.reportScreen2() {
    composable<NavigationDestination.ReportScreen2> { backStackEntry ->

        val reportAccountId: String =
            backStackEntry.toRoute<NavigationDestination.ReportScreen2>().reportAccountId
        val reportAccountHandle: String =
            backStackEntry.toRoute<NavigationDestination.ReportScreen2>().reportAccountHandle
        val reportStatusId: String? =
            backStackEntry.toRoute<NavigationDestination.ReportScreen2>().reportStatusId
        val reportType: ReportType =
            backStackEntry.toRoute<NavigationDestination.ReportScreen2>().reportType
        val checkedInstanceRules: List<InstanceRule> =
            Json.decodeFromString(
                backStackEntry.toRoute<NavigationDestination.ReportScreen2>().checkedInstanceRules
            )
        val additionalText: String =
            backStackEntry.toRoute<NavigationDestination.ReportScreen2>().additionalText
        val sendToExternalServer: Boolean =
            backStackEntry.toRoute<NavigationDestination.ReportScreen2>().sendToExternalServer

        ReportScreen2(
            reportAccountId = reportAccountId,
            reportAccountHandle = reportAccountHandle,
            reportStatusId = reportStatusId,
            reportType = reportType,
            checkedInstanceRules = checkedInstanceRules,
            additionalText = additionalText,
            sendToExternalServer = sendToExternalServer,
        )
    }
}
