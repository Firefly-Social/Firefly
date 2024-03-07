package social.firefly.feature.discover

import androidx.paging.ExperimentalPagingApi
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.navigation.navigationModule

@OptIn(ExperimentalPagingApi::class)
val discoverModule =
    module {
        includes(
            commonModule,
            navigationModule,
            analyticsModule,
        )

        viewModelOf(::DiscoverViewModel)
    }
