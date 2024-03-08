package social.firefly.feature.discover

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.postcard.PostCardUiState

sealed class DiscoverTab(val tabTitle: StringFactory, val index: Int) {
    data class Hashtags(val hashtags: Flow<PagingData<HashTagQuickViewUiState>>) :
        DiscoverTab(StringFactory.resource(R.string.hashtags_tab), 0)

    data class Posts(val posts: Flow<PagingData<PostCardUiState>>? = null) :
        DiscoverTab(StringFactory.resource(R.string.posts_tab), 1)
}