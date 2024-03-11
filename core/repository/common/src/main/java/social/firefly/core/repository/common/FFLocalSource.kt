package social.firefly.core.repository.common

interface FFLocalSource<T> {
    suspend fun saveLocally(currentPage: List<PageItem<T>>)
}