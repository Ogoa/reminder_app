package com.example.remindme.reminder.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.remindme.R
import com.example.remindme.RemindMeScreens
import com.example.remindme.reminder.data.NewReminderViewModel
import com.example.remindme.reminder.data.ReminderState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.format.DateTimeFormatter

/**
 * Displays a screen where the user can create a new reminder
 */
@Composable
fun CreateReminderScreen(
    navController: NavHostController,
    newReminderViewModel: NewReminderViewModel
) {
    val newTaskState by  newReminderViewModel.reminderState.collectAsState()

    // Format the current date into a string
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .format(newTaskState.dueDate)
        }
    }
    // Format the current time into a string
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("HH:mm")
                .format(newTaskState.dueTime)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    val context = LocalContext.current

    val MAX_CHAR_COUNT = 50


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_padding))
    ) {
        OutlinedTextField(
            value = newTaskState.title,
            onValueChange = { newReminderViewModel.updateTitle(it) },
            textStyle = MaterialTheme.typography.bodyMedium,
            label = {
                Text(
                    text = stringResource(id = R.string.task_title),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = newTaskState.description,
            onValueChange = { newReminderViewModel.updateDescription(it) },
            textStyle = MaterialTheme.typography.bodyMedium,
            label = {
                Text(
                    text = stringResource(id = R.string.task_description),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.extra_small_padding)))
        Column {
            CreateReminderDueTimeItem(
                text = formattedDate,
                buttonLabel = R.string.date_button_label,
                onClickButton = {
                    dateDialogState.show()
                }
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.extra_small_padding)))
            CreateReminderDueTimeItem(
                text = formattedTime,
                buttonLabel = R.string.time_button_label,
                onClickButton = {
                    timeDialogState.show()
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                      navController.navigate(RemindMeScreens.HomeScreen.name)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.cancel_button))
            }
            TextButton(
                onClick = {
                    val newReminder = ReminderState(
                      title = newTaskState.title,
                      description = newTaskState.description,
                      dueDate = newTaskState.dueDate,
                      dueTime = newTaskState.dueTime
                    )
                    newReminderViewModel.saveNewReminder(newReminder, navController)
                    navController.navigate(RemindMeScreens.HomeScreen.name)
                },
                enabled = newTaskState.title.isNotEmpty() && newTaskState.description.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.save_button))
            }
        }
        if(newReminderViewModel.isSaving) {
            CircularProgressIndicator()
        }
    }

    // Define MaterialDialog for date picker
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(
                text = stringResource(id = R.string.ok_button)
            ) {
                Toast.makeText(
                    context,
                    "Date saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
            negativeButton(
                text = stringResource(id = R.string.cancel_button)
            )
        }
    ) {
        datepicker(
            title = stringResource(id = R.string.pick_date_title),
            initialDate = newTaskState.dueDate,
            onDateChange = {
                newReminderViewModel.updateDueDate(it)
            }
        )
    }

    // Define MaterialDialog for time picker
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(
                text = stringResource(id = R.string.ok_button)
            ) {
                Toast.makeText(
                    context,
                    "Time saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
            negativeButton(
                text = stringResource(id = R.string.cancel_button)
            )
        }
    ) {
        timepicker(
            title = stringResource(id = R.string.time_picker_title),
            initialTime = newTaskState.dueTime,
            onTimeChange = {
                newReminderViewModel.updateDueTime(it)
            }
        )
    }
}


/**
 * Displays the due date and due time information and selection buttons in the
 * CreateReminderScreen
 * @param text the selected date or time to be displayed
 * @param buttonLabel text used to label the button
 * @param onClickButton lambda to be executed when the button is clicked
 */
@Composable
fun CreateReminderDueTimeItem(
    text: String,
    buttonLabel: Int,
    onClickButton: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding))
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onClickButton) {
            Text(text = stringResource(id = buttonLabel))
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun CreateReminderScreenPreview() {
//    RemindMeTheme {
//        CreateReminderScreen()
//    }
//}
