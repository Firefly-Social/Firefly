package social.firefly.core.repository.common

interface FFLocalSource<T: Any, DBO: Any> {
    suspend fun saveLocally(currentPage: List<PageItem<T>>)
}