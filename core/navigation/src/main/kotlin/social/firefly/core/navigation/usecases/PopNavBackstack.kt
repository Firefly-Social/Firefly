package social.firefly.core.navigation.usecases

import social.firefly.core.navigation.Event
import social.firefly.core.navigation.EventRelay
import social.firefly.core.navigation.NavigationDestination

class PopNavBackstack(
    private val eventRelay: EventRelay,
) {
    operator fun invoke(
        popUpTo: NavigationDestination? = null,
        inclusive: Boolean = false,
    ) {
        eventRelay.emitEvent(Event.PopBackStack(popUpTo, inclusive))
    }
}
