package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import social.firefly.core.database.dao.TrendingStatusDao
import social.firefly.core.database.model.entities.statusCollections.DbTrendingStatus
import social.firefly.core.model.Status
import social.firefly.core.network.mastodon.TrendsApi
import social.firefly.core.repository.common.FFLocalSource
import social.firefly.core.repository.common.FFRemoteSource
import social.firefly.core.repository.common.PageItem
import social.firefly.core.repository.mastodon.model.status.toExternalModel

@ExperimentalPagingApi
class TrendingStatusRepository(
    private val api: TrendsApi,
    private val dao: TrendingStatusDao,
) {

    fun pagingSource() = dao.pagingSource()

    suspend fun getRemotely(limit: Int, offset: Int): List<Status> =
        api.getTrendingStatuses(limit = limit, offset = offset).mapIndexed { index, status ->
            PageItem(item = status.toExternalModel(), position = index + (offset))
        }.map { it.item }

    suspend fun saveLocally(currentPage: List<PageItem<Status>>) {
        dao.upsertAll(
            currentPage.map { status ->
                DbTrendingStatus(
                    statusId = status.item.statusId,
                    position = status.position,
                )
            }
        )
    }
}
