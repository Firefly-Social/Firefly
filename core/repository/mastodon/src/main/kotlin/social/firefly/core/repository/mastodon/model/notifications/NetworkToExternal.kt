package social.firefly.core.repository.mastodon.model.notifications

import social.firefly.core.model.AccountWarning
import social.firefly.core.model.AdminReport
import social.firefly.core.model.Appeal
import social.firefly.core.model.AppealState
import social.firefly.core.model.Notification
import social.firefly.core.model.RelationshipSeveranceEvent
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccountWarning
import social.firefly.core.network.mastodon.model.responseBody.NetworkAdminReport
import social.firefly.core.network.mastodon.model.responseBody.NetworkAppeal
import social.firefly.core.network.mastodon.model.responseBody.NetworkAppealState
import social.firefly.core.network.mastodon.model.responseBody.NetworkNotification
import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationshipSeveranceEvent
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun NetworkNotification.toExternal(): Notification =
    when (this) {
        is NetworkNotification.Mention -> Notification.Mention(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is NetworkNotification.NewStatus -> Notification.NewStatus(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is NetworkNotification.Repost -> Notification.Repost(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is NetworkNotification.Follow -> Notification.Follow(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
        )

        is NetworkNotification.FollowRequest -> Notification.FollowRequest(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
        )

        is NetworkNotification.Favorite -> Notification.Favorite(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is NetworkNotification.PollEnded -> Notification.PollEnded(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is NetworkNotification.StatusUpdated -> Notification.StatusUpdated(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is NetworkNotification.AdminSignUp -> Notification.AdminSignUp(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
        )

        is NetworkNotification.AdminReport -> Notification.AdminReport(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            report = report.toExternal(),
        )

        is NetworkNotification.SeveredRelationships -> Notification.SeveredRelationships(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            severanceEvent = severanceEvent.toExternal(),
        )

        is NetworkNotification.ModerationWarning -> Notification.ModerationWarning(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            moderationWarning = moderationWarning.toExternal(),
        )

        is NetworkNotification.Quote -> Notification.Quote(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is NetworkNotification.QuoteUpdate -> Notification.QuoteUpdate(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )
    }

fun NetworkAdminReport.toExternal(): AdminReport =
    AdminReport(
        id = id,
        actionTaken = actionTaken,
        actionTakenAt = actionTakenAt,
        category = category,
        comment = comment,
        forwarded = forwarded,
        createdAt = createdAt,
        statusIds = statusIds,
        ruleIds = ruleIds,
        targetAccount = targetAccount.toExternalModel(),
    )

fun NetworkRelationshipSeveranceEvent.toExternal(): RelationshipSeveranceEvent =
    RelationshipSeveranceEvent(
        id = id,
        type = type,
        purged = purged,
        targetName = targetName,
        relationshipsCount = relationshipsCount,
        createdAt = createdAt,
    )

fun NetworkAppeal.toExternal(): Appeal =
    Appeal(
        text = text,
        state = when (state) {
            NetworkAppealState.APPROVED -> AppealState.APPROVED
            NetworkAppealState.PENDING -> AppealState.PENDING
            NetworkAppealState.REJECTED -> AppealState.REJECTED
        }
    )

fun NetworkAccountWarning.toExternal(): AccountWarning =
    AccountWarning(
        id = id,
        action = action,
        text = text,
        statusIds = statusIds,
        targetAccount = targetAccount.toExternalModel(),
        appeal = appeal?.toExternal(),
        createdAt = createdAt,
    )