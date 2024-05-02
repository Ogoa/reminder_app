package com.example.remindme.reminder.data

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.remindme.reminder.presentation.CreateReminderScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * [NewReminderViewModel] stores the state of a new reminder that is being created
 * and exposes it to [CreateReminderScreen]
 */
class NewReminderViewModel: ViewModel() {
    private val _reminderState = MutableStateFlow(ReminderState())
    val reminderState: StateFlow<ReminderState> = _reminderState.asStateFlow()

    init {
        resetReminder()
    }

    /**
     * Updates the title of the reminder
     */
    fun updateTitle(title: String) {
        _reminderState.update { currentState ->
            currentState.copy(
                title = title
            )
        }
    }

    /**
     * Updates the description of the reminder
     */
    fun updateDescription(description: String) {
        _reminderState.update{ currentState ->
            currentState.copy(
                description = description
            )
        }
    }

    /**
     * Updates the due date of the reminder being created
     */
    fun updateDueDate(date: LocalDate) {
        _reminderState.update { currentState ->
            currentState.copy(
                dueDate = date
            )
        }
    }

    /**
     * Updates the due time of the reminder being created
     */
    fun updateDueTime(time: LocalTime) {
        _reminderState.update { currentState ->
            currentState.copy(
                dueTime = time
            )
        }
    }

    /**
     * Updates the status of a task to completed or not
     */
    fun updateCompletedStatus(completed: Boolean) {
        _reminderState.update{ currentState ->
            currentState.copy(
                isCompleted = completed
            )
        }
    }

    /**
     * Resets the reminder
     */
    private fun resetReminder() {
        _reminderState.value = ReminderState()
    }
}