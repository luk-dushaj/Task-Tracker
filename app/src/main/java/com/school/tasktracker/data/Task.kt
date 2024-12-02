package com.school.tasktracker.data

import java.util.UUID

// Implemented as data class because we are managing the objects in a array
// All the standard methods like delete/create will be in the array
// editing will mainly be accessing the properties from the objects via dot notation
data class Task (
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var description: String,
    var isPriority: Boolean,
    var date: String = "10-22-2024",
    var time: String = "5:36 PM",
    var days: Int = 0,
)