package com.example.remindme.reminder.data

import androidx.compose.ui.text.toLowerCase
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


/**
 * [ReminderState] stores the state of a reminder
 * @param title title of the task for which a reminder has been set
 * @param description details of what the task is about
 * @param isCompleted whether the task has been completed or not
 */
data class ReminderState(
    val title: String = "",
    val description: String = "",
    val dueTime: LocalTime = LocalTime.now(),
    val dueDate: LocalDate = LocalDate.now(),
    val isCompleted: Boolean = false
) {
    companion object {
        /**
         * [fromFirestore] deserializes a document from the firestore database into a
         * [ReminderState] object
         */
        fun fromFirestore(
            title: String,
            description: String,
            dueTime: Map<String, Long>, // Firestore document field for localTime
            dueDate: Map<String, Any>, // Firestore document field for localDate
            isCompleted: Boolean
        ): ReminderState {
            val year = dueDate["year"] as Long
            val month = dueDate["month"] as String
            val day = dueDate["dayOfMonth"] as Long
            val localDate = LocalDate.of(year.toInt(), month.toMonthNumber(), day.toInt())

            val hour = dueTime["hour"] as Long
            val minute = dueTime["minute"] as Long
            val second = dueTime["second"] as Long
            val nano = dueTime["nano"] as Long
            val localTime = LocalTime.of(hour.toInt(), minute.toInt(), second.toInt(), nano.toInt())

            return ReminderState(title, description, localTime, localDate, isCompleted)
        }
    }
}

/**
 * Extension function to convert a month name to the equivalent month number
 */
private fun String.toMonthNumber(): Int {
    return when(this) {
        "JANUARY" -> 1
        "FEBRUARY" -> 2
        "MARCH" -> 3
        "APRIL" -> 4
        "MAY" -> 5
        "JUNE" -> 6
        "JULY" -> 7
        "AUGUST" -> 8
        "SEPTEMBER" -> 9
        "OCTOBER" -> 10
        "NOVEMBER" -> 11
        "DECEMBER" -> 12
        else -> 0
    }
}

fun ReminderState.createTimeString(): String {
    return DateTimeFormatter.ofPattern("HH:mm").format(dueTime)
}

fun ReminderState.getRelativeDateString(): String {
    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)
    val nextMonth = today.plusWeeks(4)
    val nextYear = LocalDate.now().plusYears(1)
    return when {
        dueDate == today -> "Today"
        dueDate == tomorrow -> "Tomorrow"
        dueDate >= nextYear || dueDate > nextMonth -> DateTimeFormatter
            .ofPattern("E, d MMM yyyy", Locale.ENGLISH)
            .format(dueDate)
        else -> DateTimeFormatter.ofPattern("E, d MMM", Locale.ENGLISH).format(dueDate)
    }
}
