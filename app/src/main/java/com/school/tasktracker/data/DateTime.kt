package com.school.tasktracker.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.String.format
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


class DateTime {

    @RequiresApi(Build.VERSION_CODES.O)
    public fun encodeDate(date:String):LocalDate{
        //converts string to date
        var formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")

        return LocalDate.parse(date,formatter)


    }
    @RequiresApi(Build.VERSION_CODES.O)
    public fun decodeDate(date: LocalDate):String{
        //converts date to strings
        var formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun encodeTime(time:String):LocalDate{
        //converts string to date
        var formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return LocalDate.parse(time,formatter)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    public fun decodeTime(time: LocalDate):String{
        //converts date to strings
        var formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return time.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDays(dueDate: String): Int {
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val current = LocalDate.now() // Current date
        val due = LocalDate.parse(dueDate, formatter) // Parse due date

        return ChronoUnit.DAYS.between(current, due).toInt()
    }

}