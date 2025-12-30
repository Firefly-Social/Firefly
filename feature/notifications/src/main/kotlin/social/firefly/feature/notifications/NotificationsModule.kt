package social.firefly.feature.notifications

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.repository.paging.pagingModule
import social.firefly.core.ui.notifications.notificationUiModule
import social.firefly.core.ui.postcard.postCardModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val notificationsModule = module {
    includes(
        notificationUiModule,
        mastodonRepositoryModule,
        mastodonUsecaseModule,
        pagingModule,
        navigationModule,
        commonModule,
        postCardModule,
        analyticsModule,
    )
    viewModelOf(::NotificationsViewModel)
}