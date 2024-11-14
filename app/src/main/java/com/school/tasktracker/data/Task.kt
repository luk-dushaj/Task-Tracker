package com.school.tasktracker.data

import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// Implemented as data class because we are managing the objects in a array
// All the standard methods like delete/create will be in the array
// editing will mainly be accessing the properties from the objects via dot notation
class Task(
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var description: String,
    var isPriority: Boolean,
    // those below are strings since Date() doesn't have a attribute to save those
    // So basically will encode the date as a string and will decode it back into a date from the string
    var date: String,
    var time: String,
    var isDone: Boolean = false
) {
    fun decodeDateTime(dateTime: DateTime): Pair<Date?, Date?> {
        // Initialize the same formatters used in encoding
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Parse the date and time strings
        val date = dateFormat.parse(date)  // Returns null if parsing fails
        val time = timeFormat.parse(time)  // Returns null if parsing fails

        return Pair(date, time)
    }
}