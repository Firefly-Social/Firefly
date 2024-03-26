package social.firefly.core.repository.paging.common

interface FRemoteSource<T> {
    suspend fun getRemotely(limit: Int, offset: Int): List<T>
}