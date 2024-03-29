package social.firefly.core.test.fakes

import social.firefly.core.model.Application

fun fakeApplication(
    name: String = "Louella Perry",
    website: String? = null,
    vapidKey: String? = null,
    clientId: String? = null,
    clientSecret: String? = null,
) = Application(
    name = name,
    website = website,
    vapidKey = vapidKey,
    clientId = clientId,
    clientSecret = clientSecret,
)
