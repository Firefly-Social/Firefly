package social.firefly.core.model

data class QuoteApproval(
    val automatic: List<QuoteApprovalGroup>,
    val manual: List<QuoteApprovalGroup>,
    val currentUser: QuoteApprovalType,
)

enum class QuoteApprovalGroup {
    PUBLIC,
    FOLLOWERS,
    FOLLOWING,
    UNSUPPORTED_POLICY,
}

enum class QuoteApprovalType {
    AUTOMATIC,
    MANUAL,
    DENIED,
    UNKNOWN,
}