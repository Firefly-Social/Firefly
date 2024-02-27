package social.firefly.core.network.mastodon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkWebPushSubscription(
    @SerialName("id")
    val id: Int,
    @SerialName("endpoint")
    val endpoint: String,
    @SerialName("server_key")
    val serverKey: String,
)