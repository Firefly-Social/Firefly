package social.firefly.core.network.mastodon.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class NetworkQuoteApprovalPolicy {
    @SerialName("public")
    PUBLIC,
    @SerialName("followers")
    FOLLOWERS,
    @SerialName("nobody")
    NOBODY,
}