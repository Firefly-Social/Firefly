package social.firefly.core.navigation

import kotlinx.coroutines.flow.SharedFlow

class NavigationEventFlow(private val eventRelay: EventRelay) {
    operator fun invoke(): SharedFlow<Event> = eventRelay.navigationEvents
}
