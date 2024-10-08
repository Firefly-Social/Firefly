package social.firefly.core.ui.postcard

import social.firefly.core.ui.htmlcontent.HtmlContentInteractions
import social.firefly.core.ui.poll.PollInteractions

interface PostCardInteractions : PollInteractions, HtmlContentInteractions {
    fun onReplyClicked(statusId: String)

    fun onBoostClicked(
        statusId: String,
        isBoosting: Boolean,
    )

    fun onFavoriteClicked(
        statusId: String,
        isFavoriting: Boolean,
    )

    fun onBookmarkClicked(
        statusId: String,
        isBookmarking: Boolean,
    )

    fun onPostCardClicked(statusId: String)

    fun onOverflowMuteClicked(
        accountId: String,
        statusId: String,
    )

    fun onOverflowBlockClicked(
        accountId: String,
        statusId: String,
    )

    fun onOverflowReportClicked(
        accountId: String,
        accountHandle: String,
        statusId: String,
    )

    fun onOverflowBlockDomainClicked(
        domain: String,
    )

    fun onOverflowDeleteClicked(statusId: String)

    fun onOverflowEditClicked(statusId: String)

    fun onAccountImageClicked(accountId: String)

    fun onMediaClicked(
        statusId: String,
        index: Int,
    )

    fun onHideRepliesClicked(
        statusId: String,
    )
}

object PostCardInteractionsNoOp: PostCardInteractions {
    override fun onReplyClicked(statusId: String) = Unit

    override fun onBoostClicked(statusId: String, isBoosting: Boolean) = Unit

    override fun onFavoriteClicked(statusId: String, isFavoriting: Boolean) = Unit

    override fun onBookmarkClicked(statusId: String, isBookmarking: Boolean) = Unit

    override fun onPostCardClicked(statusId: String) = Unit

    override fun onOverflowMuteClicked(accountId: String, statusId: String) = Unit

    override fun onOverflowBlockClicked(accountId: String, statusId: String) = Unit

    override fun onOverflowReportClicked(
        accountId: String,
        accountHandle: String,
        statusId: String
    ) = Unit

    override fun onOverflowBlockDomainClicked(domain: String) = Unit

    override fun onOverflowDeleteClicked(statusId: String) = Unit

    override fun onOverflowEditClicked(statusId: String) = Unit

    override fun onAccountImageClicked(accountId: String) = Unit

    override fun onMediaClicked(statusId: String, index: Int) = Unit

    override fun onHideRepliesClicked(statusId: String) = Unit
}