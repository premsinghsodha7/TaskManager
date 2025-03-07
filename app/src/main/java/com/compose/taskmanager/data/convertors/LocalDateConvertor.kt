package com.compose.taskmanager.data.convertors

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

/**
 * [LocalDateConverter] is a concrete implementation of [BaseConverter] that handles the conversion
 * between a String representation and a [LocalDate] object.
 *
 * This converter is specifically designed to work with date strings in the ISO_LOCAL_DATE format
 * (e.g., "2023-10-27"). It leverages the `LocalDate.parse()` method for parsing.
 *
 * Note: This class requires API level 26 (Android O) or higher due to its use of the `java.time` package.
 *
 * @constructor Creates a new instance of [LocalDateConverter].
 *
 * @see BaseConverter
 * @see LocalDate
 */
class LocalDateConverter : BaseConverter<LocalDate>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun objectFromString(value: String): LocalDate? = try {
        LocalDate.parse(value)
    } catch (e: Exception) {
        null
    }
}
