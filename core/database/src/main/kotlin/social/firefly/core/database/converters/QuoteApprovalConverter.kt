package social.firefly.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import social.firefly.core.database.model.DatabaseQuoteApproval

class QuoteApprovalConverter {
    @TypeConverter
    fun quoteApprovalToJson(value: DatabaseQuoteApproval): String = Json.encodeToString(
        DatabaseQuoteApproval.serializer(),
        value
    )

    @TypeConverter
    fun jsonToQuoteApproval(string: String): DatabaseQuoteApproval = Json.decodeFromString(
        DatabaseQuoteApproval.serializer(),
        string
    )
}