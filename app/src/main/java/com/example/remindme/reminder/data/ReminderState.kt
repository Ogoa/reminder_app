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
)
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
