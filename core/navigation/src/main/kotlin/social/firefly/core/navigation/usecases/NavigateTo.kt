package social.firefly.core.navigation.usecases

import androidx.navigation.NavOptions
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.BottomBarNavigationDestination
import social.firefly.core.navigation.EventRelay
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.SettingsNavigationDestination

class NavigateTo(
    private val eventRelay: EventRelay,
) {
    operator fun invoke(
        navDestination: NavigationDestination,
        navOptions: NavOptions? = null,
    ) {
        eventRelay.emitEvent(navDestination, navOptions)
    }

    operator fun invoke(
        navDestination: BottomBarNavigationDestination,
        navOptions: NavOptions? = null,
    ) {
        eventRelay.emitEvent(navDestination, navOptions)
    }

    operator fun invoke(
        navDestination: SettingsNavigationDestination,
        navOptions: NavOptions? = null,
    ) {
        eventRelay.emitEvent(navDestination, navOptions)
    }

    operator fun invoke(
        navDestination: AuthNavigationDestination,
        navOptions: NavOptions? = null,
    ) {
        eventRelay.emitEvent(navDestination, navOptions)
    }
}
