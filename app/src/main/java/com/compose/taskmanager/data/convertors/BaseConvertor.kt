package com.compose.taskmanager.data.convertors

import androidx.room.TypeConverter

/**
 * An abstract base class for creating type converters for Room database.
 *
 * This class provides a foundation for converting between a custom type `T` and its String representation,
 * which is necessary for storing custom types in Room databases.
 *
 * Subclasses should extend this class and override the [objectFromString] method to implement
 * the specific logic for converting a String back into the desired type `T`.
 *
 * The [toString] and [fromString] methods are provided with default implementations that can be
 * overridden if needed.
 *
 * @param T The type of object this converter handles.
 */
abstract class BaseConverter<T> {

    /**
     * Converts the [value] to a String. The default implementation is to call [toString].
     *  This can be overridden.
     */
    @TypeConverter
    open fun toString(value: T?): String? = value?.toString()

    /**
     * Converts the [value] to a [T]. If the [value] is null or empty, the returned [T] is null.
     *  If not, this called [objectFromString]. This can be overridden.
     */
    @TypeConverter
    open fun fromString(value: String?): T? =
        if (value.isNullOrEmpty()) null else objectFromString(value)

    /**
     * Converts the [value] to a [T]
     */
    abstract fun objectFromString(value: String): T?
}