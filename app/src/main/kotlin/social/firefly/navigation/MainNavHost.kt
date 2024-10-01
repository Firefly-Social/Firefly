package social.firefly.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import social.firefly.common.utils.ffFadeIn
import social.firefly.common.utils.ffFadeOut
import social.firefly.feature.account.accountScreen
import social.firefly.feature.account.edit.editAccountScreen
import social.firefly.feature.auth.authFlow
import social.firefly.feature.bookmarks.bookmarksScreen
import social.firefly.feature.favorites.favoritesScreen
import social.firefly.feature.followedHashTags.followedHashTagsScreen
import social.firefly.feature.followers.followersScreen
import social.firefly.feature.hashtag.hashTagScreen
import social.firefly.feature.media.mediaScreen
import social.firefly.feature.settings.settingsFlow
import social.firefly.feature.thread.threadScreen
import social.firefly.feature.post.newPostScreen
import social.firefly.feature.report.step1.reportScreen1
import social.firefly.feature.report.step2.reportScreen2
import social.firefly.feature.report.step3.reportScreen3
import social.firefly.feature.search.searchScreen
import social.firefly.splash.splashScreen
import social.firefly.ui.AppState
import social.firefly.ui.bottombar.Routes
import social.firefly.ui.bottombar.bottomTabScreen

@Composable
fun MainNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = appState.mainNavController,
        startDestination = Routes.SPLASH,
        enterTransition = { ffFadeIn() },
        exitTransition = { ffFadeOut() },
        popEnterTransition = { ffFadeIn() },
        popExitTransition = { ffFadeOut() },
    ) {
        splashScreen()
        authFlow()
        accountScreen()
        followersScreen()
        newPostScreen()
        threadScreen()
        reportScreen1()
        reportScreen2()
        reportScreen3()
        hashTagScreen()
        bottomTabScreen(appState)
        editAccountScreen()
        settingsFlow()
        favoritesScreen()
        searchScreen()
        mediaScreen()
        followedHashTagsScreen()
        bookmarksScreen()
    }
}


