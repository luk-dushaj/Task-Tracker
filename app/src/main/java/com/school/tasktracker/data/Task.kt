package com.school.tasktracker.data

import com.google.type.Color
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// Implemented as data class because we are managing the objects in a array
// All the standard methods like delete/create will be in the array
// editing will mainly be accessing the properties from the objects via dot notation
data class Task (
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var description: String,
    var isPriority: Boolean,
    // those below are strings since Date() doesn't have a attribute to save those
    // So basically will encode the date as a string and will decode it back into a date from the string
    var date: String = "10-22-2024",
    var time: String = "5:36 PM",
    // Days defaulting to 0 because evenetually I have to add a function to calculate how many days are left
    var days: Int = 0,
    var isDone: Boolean = false
)