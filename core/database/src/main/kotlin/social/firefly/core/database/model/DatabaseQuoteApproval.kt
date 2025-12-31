package social.firefly.core.database.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DatabaseQuoteApproval(
    val automatic: List<DatabaseQuoteApprovalGroup>,
    val manual: List<DatabaseQuoteApprovalGroup>,
    val currentUser: DatabaseQuoteApprovalType,
)

@Serializable
enum class DatabaseQuoteApprovalGroup {
    @SerialName("public")
    PUBLIC,

    @SerialName("followers")
    FOLLOWERS,

    @SerialName("following")
    FOLLOWING,

    @SerialName("unsupported_policy")
    UNSUPPORTED_POLICY,
}

@Serializable
enum class DatabaseQuoteApprovalType {
    @SerialName("automatic")
    AUTOMATIC,

    @SerialName("manual")
    MANUAL,

    @SerialName("denied")
    DENIED,

    @SerialName("unknown")
    UNKNOWN,
}