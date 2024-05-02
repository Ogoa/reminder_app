package com.example.remindme.reminder.data

import java.time.LocalDate
import java.time.LocalTime


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
