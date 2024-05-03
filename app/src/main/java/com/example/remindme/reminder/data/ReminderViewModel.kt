package com.example.remindme.reminder.data

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindme.reminder.presentation.CreateReminderScreen
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val TAG = "ReminderViewModel"

/**
 * [NewReminderViewModel] stores the state of a new reminder that is being created
 * and exposes it to [CreateReminderScreen]
 */
class NewReminderViewModel(application: Application): ViewModel() {
    private val _reminderState = MutableStateFlow(ReminderState())
    val reminderState: StateFlow<ReminderState> = _reminderState.asStateFlow()
    val appContext = application.applicationContext

    var isSaving by mutableStateOf(false)
        private set

    // Create a reminders collection
    private val remindersCollection = Firebase.firestore.collection("reminders")

    /**
     * Saves the reminder in firestore
     */
    fun saveNewReminder(reminder: ReminderState) = viewModelScope.launch(Dispatchers.IO) {
        isSaving = true
        remindersCollection
            .add(reminder)
            .addOnSuccessListener {
                // Launch on the Main thread to display a Toast message
                viewModelScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        appContext,
                        "Successfully saved the reminder",
                        Toast.LENGTH_LONG
                    ).show()
                    isSaving = false
                }
                Log.d(TAG, "Successfully saved reminder")
                // Reset the viewModel after saving the previously created reminder
                resetReminder()
            }
            .addOnFailureListener {
                Log.d(TAG, "${it.message}")
                isSaving = false
            }
    }

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