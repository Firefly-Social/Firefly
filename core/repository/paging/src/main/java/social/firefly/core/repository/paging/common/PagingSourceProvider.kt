package social.firefly.core.repository.paging.common

import androidx.paging.PagingSource

interface PagingSourceProvider<DBO: Any> {
    fun pagingSource(): PagingSource<Int, DBO>
}