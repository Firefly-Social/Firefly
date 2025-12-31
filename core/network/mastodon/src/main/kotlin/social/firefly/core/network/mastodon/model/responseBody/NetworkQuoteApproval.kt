package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkQuoteApproval(
    @SerialName("automatic")
    val automatic: List<NetworkQuoteApprovalGroup>,
    @SerialName("manual")
    val manual: List<NetworkQuoteApprovalGroup>,
    @SerialName("current_user")
    val currentUser: NetworkQuoteApprovalType,
)

enum class NetworkQuoteApprovalGroup {
    @SerialName("public")
    PUBLIC,

    @SerialName("followers")
    FOLLOWERS,

    @SerialName("following")
    FOLLOWING,

    @SerialName("unsupported_policy")
    UNSUPPORTED_POLICY,
}

enum class NetworkQuoteApprovalType {
    @SerialName("automatic")
    AUTOMATIC,

    @SerialName("manual")
    MANUAL,

    @SerialName("denied")
    DENIED,

    @SerialName("unknown")
    UNKNOWN,
}