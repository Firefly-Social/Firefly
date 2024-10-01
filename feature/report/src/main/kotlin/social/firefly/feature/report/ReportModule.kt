package social.firefly.feature.report

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule
import social.firefly.feature.report.step1.ReportScreen1ViewModel
import social.firefly.feature.report.step2.ReportScreen2ViewModel
import social.firefly.feature.report.step3.ReportScreen3ViewModel

val reportModule =
    module {
        includes(
            commonModule,
            mastodonRepositoryModule,
            dataStoreModule,
            navigationModule,
            analyticsModule,
            mastodonUsecaseModule,
        )

        viewModel { parametersHolder ->
            ReportScreen1ViewModel(
                get(),
                get(),
                get(),
                get(),
                parametersHolder[0],
                parametersHolder[1],
                parametersHolder[2],
            )
        }
        viewModel { parametersHolder ->
            ReportScreen2ViewModel(
                report = get(),
                accountRepository = get(),
                navigateTo = get(),
                popNavBackstack = get(),
                reportAccountId = parametersHolder[0],
                reportAccountHandle = parametersHolder[1],
                reportStatusId = parametersHolder[2],
                reportType = parametersHolder[3],
                checkedInstanceRules = parametersHolder[4],
                additionalText = parametersHolder[5],
                sendToExternalServer = parametersHolder[6],
            )
        }
        viewModel { parametersHolder ->
            ReportScreen3ViewModel(
                unfollowAccount = get(),
                blockAccount = get(),
                muteAccount = get(),
                popNavBackstack = get(),
                eventRelay = get(),
                getLoggedInUserAccountId = get(),
                reportAccountId = parametersHolder[0],
                didUserReportAccount = parametersHolder[1],
            )
        }
    }
