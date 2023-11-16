package org.mozilla.social.core.database.model.accountCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseRelationship

/**
 * @param accountId the account ID of the user we are examining
 * @param followerAccountId the account ID of the follower
 */
@Entity(
    tableName = "followers",
    primaryKeys = [
        "accountId",
        "followerAccountId",
    ]
)
data class Follower(
    val accountId: String,
    val followerAccountId: String,
)

data class FollowerWrapper(
    @Embedded
    val follower: Follower,

    @Relation(
        parentColumn = "followerAccountId",
        entityColumn = "accountId",
    )
    val followerAccount: DatabaseAccount,

    @Relation(
        parentColumn = "followerAccountId",
        entityColumn = "accountId",
    )
    val relationship: DatabaseRelationship,
)