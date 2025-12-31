package social.firefly.feature.post

import social.firefly.core.model.QuoteApprovalPolicy
import social.firefly.core.model.StatusVisibility

interface NewPostInteractions {
    fun onScreenViewed()
    fun onUploadImageClicked()
    fun onUploadMediaClicked()
    fun onPostClicked()
    fun onEditClicked()
    fun onVisibilitySelected(statusVisibility: StatusVisibility)
    fun onQuoteApprovalPolicySelected(quoteApprovalPolicy: QuoteApprovalPolicy)
    fun onLanguageSelected(code: String)
}

object NewPostInteractionsNoOp : NewPostInteractions {
    override fun onScreenViewed() = Unit
    override fun onUploadImageClicked() = Unit
    override fun onUploadMediaClicked() = Unit
    override fun onPostClicked() = Unit
    override fun onEditClicked() = Unit
    override fun onVisibilitySelected(statusVisibility: StatusVisibility) = Unit
    override fun onQuoteApprovalPolicySelected(quoteApprovalPolicy: QuoteApprovalPolicy) = Unit
    override fun onLanguageSelected(code: String) = Unit
}
