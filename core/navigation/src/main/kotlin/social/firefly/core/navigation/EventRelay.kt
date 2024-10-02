package social.firefly.core.navigation

import androidx.navigation.NavOptions
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import social.firefly.common.utils.StringFactory
import timber.log.Timber

class EventRelay {
    private val _navigationEvents = MutableSharedFlow<Event>(
        replay = 0,
        extraBufferCapacity = 10,
    )
    val navigationEvents: SharedFlow<Event>
        get() = _navigationEvents

    fun emitEvent(
        navDestination: NavigationDestination,
        navOptions: NavOptions? = null,
    ) {
        emitEvent(Event.NavigateToDestination(navDestination, navOptions))
    }

    fun emitEvent(
        navDestination: BottomBarNavigationDestination,
        navOptions: NavOptions? = null,
    ) {
        emitEvent(Event.NavigateToBottomBarDestination(navDestination, navOptions))
    }

    fun emitEvent(
        navDestination: SettingsNavigationDestination,
        navOptions: NavOptions? = null,
    ) {
        emitEvent(Event.NavigateToSettingsDestination(navDestination, navOptions))
    }

    fun emitEvent(
        navDestination: AuthNavigationDestination,
        navOptions: NavOptions? = null,
    ) {
        emitEvent(Event.NavigateToLoginDestination(navDestination, navOptions))
    }

    fun emitEvent(event: Event) {
        Timber.d("NAVIGATION trying to emit $event")
        _navigationEvents.tryEmit(event)
    }
}

sealed class Event {
    data object PopBackStack : Event()

    data class OpenLink(val url: String) : Event()

    data class ShowSnackbar(val text: StringFactory, val isError: Boolean) : Event()

    data class NavigateToDestination(
        val destination: NavigationDestination,
        val navOptions: NavOptions? = null,
    ) : Event()

    data class NavigateToBottomBarDestination(
        val destination: BottomBarNavigationDestination,
        val navOptions: NavOptions? = null,
    ) : Event()

    data class NavigateToSettingsDestination(
        val destination: SettingsNavigationDestination,
        val navOptions: NavOptions? = null,
    ) : Event()

    data class NavigateToLoginDestination(
        val destination: AuthNavigationDestination,
        val navOptions: NavOptions? = null,
    ) : Event()

    data object ChooseAccountForSharing : Event()

    data object ExitReportFlow : Event()
}
