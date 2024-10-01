package social.firefly.ui

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.BottomBarNavigationDestination
import social.firefly.core.navigation.Event
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.NavigationEventFlow
import social.firefly.core.navigation.SettingsNavigationDestination
import social.firefly.core.ui.common.snackbar.FfSnackbarHostState
import social.firefly.core.ui.common.snackbar.SnackbarType
import timber.log.Timber

@Composable
fun rememberAppState(
    mainNavController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: FfSnackbarHostState = remember { FfSnackbarHostState() },
    navigationEventFlow: NavigationEventFlow = koinInject(),
): AppState {
    val context = LocalContext.current

    return remember(mainNavController) {
        AppState(
            mainNavController = mainNavController,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            context = context,
            navigationEventFlow = navigationEventFlow,
        )
    }
}

/**
 * Class to encapsulate high-level app state
 */
class AppState(
    initialBottomBarDestination: BottomBarNavigationDestination = BottomBarNavigationDestination.Feed,
    val mainNavController: NavHostController,
    val coroutineScope: CoroutineScope,
    val snackbarHostState: FfSnackbarHostState,
    val context: Context,
    val navigationEventFlow: NavigationEventFlow,
) {
    // The tabbedNavController is stored in a StateFlow so that we can make sure there's no
    // requests to navigate until the BottomTabNavHost has reached composition
    private val _tabbedNavControllerFlow = MutableStateFlow<NavHostController?>(null)
    val tabbedNavControllerFlow: StateFlow<NavHostController?> =
        _tabbedNavControllerFlow.asStateFlow()

    // Convenience var for getting/setting value in flow
    var tabbedNavController: NavHostController?
        get() = tabbedNavControllerFlow.value
        set(value) {
            _tabbedNavControllerFlow.value = value
        }

    // Use when navigating back from report screen 3.
    private var reportDestination: NavigationDestination.ReportScreen1? = null

    init {
        coroutineScope.launch(Dispatchers.Main) {
            navigationEventFlow().onSubscription {
                navigationCollectionCompletable.complete(Unit)
            }.onCompletion {
                // reset when the app is closed via the back button
                navigationCollectionCompletable = CompletableDeferred()
            }.collectLatest {
                Timber.d("NAVIGATION consuming event $it")
                when (it) {
                    is Event.NavigateToDestination -> {
                        navigate(it.destination, it.navOptions)
                    }

                    is Event.NavigateToBottomBarDestination -> {
                        navigateToBottomBarDestination(it.destination)
                    }

                    is Event.NavigateToSettingsDestination -> {
                        navigateToSettingsDestination(it.destination, it.navOptions)
                    }

                    is Event.NavigateToLoginDestination -> {
                        navigateToAuthDestination(it.destination, it.navOptions)
                    }

                    is Event.PopBackStack -> {
                        popBackStack(
                            popUpTo = it.popUpTo,
                            inclusive = it.inclusive,
                        )
                    }

                    is Event.OpenLink -> {
                        openLink(it.url)
                    }

                    is Event.ShowSnackbar -> {
                        showSnackbar(it.text, it.isError)
                    }

                    is Event.ChooseAccountForSharing -> {
                        // Handled in ChooseAccountDialogViewModel
                    }

                    is Event.ExitReportFlow -> {
                        reportDestination?.let { reportDestination ->
                            mainNavController.popBackStack(reportDestination, true)
                        }
                    }
                }
            }
        }
    }

    private fun showSnackbar(
        text: StringFactory,
        error: Boolean,
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                snackbarType = if (error) SnackbarType.ERROR else SnackbarType.SUCCESS,
                message = text.build(context),
            )
        }
    }

    private fun openLink(url: String) {
        var uri = url.toUri()

        try {
            if (uri.scheme.isNullOrBlank()) {
                uri = uri.buildUpon().scheme(HTTPS_SCHEME).build()
            }

            CustomTabsIntent.Builder()
                .build()
                .launchUrl(
                    context,
                    uri,
                )
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentNavigationDestination: StateFlow<String?> =
        mainNavController.currentBackStackEntryFlow.mapLatest { backStackEntry ->
            backStackEntry.destination.route
        }.stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    var currentBottomBarNavigationDestination: StateFlow<String?> =
        tabbedNavControllerFlow.flatMapLatest { navHostController ->
            navHostController?.currentBackStackEntryFlow?.mapLatest { backStackEntry ->
                backStackEntry.destination.route
            } ?: error("no matching nav destination")
        }.stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialBottomBarDestination::class.qualifiedName,
        )

    private fun clearBackstack() {
        while (mainNavController.currentBackStack.value.isNotEmpty()) {
            mainNavController.popBackStack()
        }
    }

    private fun popBackStack(
        popUpTo: NavigationDestination? = null,
        inclusive: Boolean = false,
    ) {
        popUpTo?.let {
            if (!mainNavController.popBackStack(popUpTo, inclusive)) tabbedNavController?.navigateUp()
        }
        if (!mainNavController.navigateUp()) tabbedNavController?.navigateUp()
    }

    @Suppress("CyclomaticComplexMethod")
    private fun navigate(
        navDestination: NavigationDestination,
        navOptions: NavOptions?,
    ) {
        Timber.d("NAVIGATION consuming $navDestination")
        with(navDestination) {
            when (this) {
                NavigationDestination.Auth -> {
                    clearBackstack()
                    mainNavController.navigate(navDestination, navOptions)
                }

                NavigationDestination.Tabs -> {
                    clearBackstack()
                    mainNavController.navigate(navDestination, navOptions)
                }

                is NavigationDestination.ReportScreen1 -> {
                    reportDestination = navDestination as NavigationDestination.ReportScreen1
                    mainNavController.navigate(navDestination, navOptions)
                }

                else -> mainNavController.navigate(navDestination, navOptions)
            }
        }
    }

    private fun navigateToSettingsDestination(
        destination: SettingsNavigationDestination,
        navOptions: NavOptions?,
    ) {
        mainNavController.navigate(destination, navOptions)
    }

    private fun navigateToAuthDestination(
        destination: AuthNavigationDestination,
        navOptions: NavOptions?,
    ) {
        mainNavController.navigate(destination, navOptions)
    }

    private fun navigateToBottomBarDestination(destination: BottomBarNavigationDestination) {
        Timber.d("NAVIGATION navigate to bottom bar destination: $destination")

        // If navigating to the feed, just pop up to the feed.  Don't start a new instance
        // of it.  If a new instance is started, we don't retain scroll position!
        if (destination == BottomBarNavigationDestination.Feed) {
            tabbedNavController?.popBackStack(
                BottomBarNavigationDestination.Feed,
                false,
            )
        }
        val navOptions =
            navOptions {
                popUpTo(BottomBarNavigationDestination.Feed) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

        tabbedNavController?.navigate(destination, navOptions)
    }

    companion object {
        private const val HTTPS_SCHEME = "https"

        // complete when the navigation event flow has started
        var navigationCollectionCompletable = CompletableDeferred<Unit>()
            private set
    }
}
