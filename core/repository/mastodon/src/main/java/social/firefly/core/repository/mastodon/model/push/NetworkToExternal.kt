package social.firefly.core.repository.mastodon.model.push

import social.firefly.core.model.WebPushSubscription
import social.firefly.core.network.mastodon.model.NetworkWebPushSubscription

fun NetworkWebPushSubscription.toExternal(): WebPushSubscription = WebPushSubscription(
    id = id,
    endpoint = endpoint,
    serverKey = serverKey,
)