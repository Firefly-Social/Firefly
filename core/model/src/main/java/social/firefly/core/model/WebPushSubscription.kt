package social.firefly.core.model

data class WebPushSubscription(
    val id: Int,
    val endpoint: String,
    val serverKey: String,
)
