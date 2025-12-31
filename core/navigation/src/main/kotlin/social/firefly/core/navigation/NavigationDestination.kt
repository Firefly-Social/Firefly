package social.firefly.core.navigation

import kotlinx.serialization.Serializable
import social.firefly.core.model.ReportType

/**
 * Represents a top-level Navigation destination
 */
@Serializable
sealed class NavigationDestination {
    @Serializable
    data object Splash : NavigationDestination()

    @Serializable
    data class Account(val accountId: String) : NavigationDestination()

    @Serializable
    data object EditAccount : NavigationDestination()

    @Serializable
    data object Auth : NavigationDestination()

    @Serializable
    data object Bookmarks : NavigationDestination()

    @Serializable
    data object Tabs : NavigationDestination()

    @Serializable
    data object Favorites : NavigationDestination()

    @Serializable
    data object FollowedHashTags : NavigationDestination()

    @Serializable
    data class Followers(
        val accountId: String,
        val displayName: String,
        val startingTab: StartingTab,
    ) : NavigationDestination() {
        enum class StartingTab(
            val value: String,
        ) {
            FOLLOWERS("followers"),
            FOLLOWING("following"),
        }
    }

    @Serializable
    data class HashTag(val hashtag: String) : NavigationDestination()

    @Serializable
    data class Media(
        val statusId: String,
        val startIndex: Int = 0,
    ) : NavigationDestination()

    @Serializable
    data class NewPost(
        val replyStatusId: String? = null,
        val editStatusId: String? = null,
        val quoteStatusId: String? = null,
    ) : NavigationDestination()

    @Serializable
    data class ReportScreen1(
        val reportAccountId: String,
        val reportAccountHandle: String,
        val reportStatusId: String? = null,
    ) : NavigationDestination()

    @Serializable
    data class ReportScreen2(
        val reportAccountId: String,
        val reportAccountHandle: String,
        val reportStatusId: String?,
        val reportType: ReportType,
        val checkedInstanceRules: String,
        val additionalText: String,
        val sendToExternalServer: Boolean,
    ) : NavigationDestination()

    @Serializable
    data class ReportScreen3(
        val reportAccountId: String,
        val reportAccountHandle: String,
        val didUserReportAccount: Boolean,
    ) : NavigationDestination()

    @Serializable
    data object Search : NavigationDestination()

    @Serializable
    data object Settings : NavigationDestination()

    @Serializable
    data class Thread(val threadStatusId: String) : NavigationDestination()
}
