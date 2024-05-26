package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mastodon.MutesApi
import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccount
import social.firefly.core.network.mastodon.utils.toResponse

class MutesApiImpl(
    private val client: HttpClient,
) : MutesApi {
    override suspend fun getMutes(
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int?
    ): Response<List<NetworkAccount>> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/mutes")
            parameters.apply {
                maxId?.let { append("max_id", it) }
                sinceId?.let { append("since_id", it) }
                minId?.let { append("min_id", it) }
                limit?.let { append("limit", it.toString()) }
            }
        }
    }.toResponse()
}