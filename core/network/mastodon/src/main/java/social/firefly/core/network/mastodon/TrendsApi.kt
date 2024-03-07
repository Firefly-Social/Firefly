package social.firefly.core.network.mastodon

import retrofit2.http.GET
import retrofit2.http.Query
import social.firefly.core.network.mastodon.model.NetworkHashTag
import social.firefly.core.network.mastodon.model.NetworkLink
import social.firefly.core.network.mastodon.model.NetworkStatus

interface TrendsApi {

    @GET("/api/v1/trends/tags")
    suspend fun getTrendingTags(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
    ): List<NetworkHashTag>

    @GET("/api/v1/trends/statuses")
    suspend fun getTrendingStatuses(
        limit: Int? = null,
        offset: Int? = null,
    ): List<NetworkStatus>

    @GET("/api/v1/trends/links")
    suspend fun getTrendingLinks(
        limit: Int? = null,
        offset: Int? = null,
    ): List<NetworkLink>
}