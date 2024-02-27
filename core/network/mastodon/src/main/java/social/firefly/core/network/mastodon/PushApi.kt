package social.firefly.core.network.mastodon

import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import social.firefly.core.network.mastodon.model.NetworkWebPushSubscription

interface PushApi {

    @FormUrlEncoded
    @POST("/api/v1/push/subscription")
    suspend fun subscribe(
        @Field("subscription[endpoint]") endpoint: String,
        @Field("subscription[keys][p256dh]") p256dh: String,
        @Field("subscription[keys][auth]") auth: String,
        @FieldMap data: Map<String, Boolean> = mapOf(
            Pair("[alerts][mention]", true),
            Pair("[alerts][status]", true),
            Pair("[alerts][reblog]", true),
            Pair("[alerts][follow]", true),
            Pair("[alerts][follow_request]", true),
            Pair("[alerts][favourite]", true),
            Pair("[alerts][poll]", true),
            Pair("[alerts][update]", true),
            Pair("[alerts][admin.sign_up]", false),
            Pair("[alerts][admin.report]", false),
        ),
        @Field("data[policy]") policy: String = "all"
    ): NetworkWebPushSubscription
}