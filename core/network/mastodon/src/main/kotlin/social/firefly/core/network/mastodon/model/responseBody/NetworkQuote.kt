package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkQuote(
    @SerialName("state")
    val state: String? = null,

    @SerialName("quoted_status")
    val quotedStatus: NetworkStatus? = null,
)