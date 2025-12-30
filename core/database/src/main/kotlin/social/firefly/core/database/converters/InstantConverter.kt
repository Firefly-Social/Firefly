package social.firefly.core.database.converters

import androidx.room.TypeConverter
import kotlin.time.Instant

class InstantConverter {
    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun toLong(value: Instant?): Long? = value?.toEpochMilliseconds()
}
