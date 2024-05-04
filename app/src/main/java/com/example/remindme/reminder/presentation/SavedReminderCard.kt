package com.example.remindme.reminder.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.remindme.R
import com.example.remindme.reminder.data.ReminderState
import com.example.remindme.reminder.data.createTimeString
import com.example.remindme.reminder.data.getRelativeDateString
import com.example.remindme.ui.theme.RemindMeTheme
import java.time.LocalDate


/**
 * Displays a saved reminder
 */
@Composable
fun SavedReminderCard(
    reminder: ReminderState,
    modifier: Modifier = Modifier
) {
    var radioButtonSelected by rememberSaveable { mutableStateOf(false) }

    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.small_padding))
        ) {
            RadioButton(
                selected = radioButtonSelected,
                onClick = {
                    radioButtonSelected = !radioButtonSelected
                }
            )
            Column {
                // Title of the reminder
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                // Description of the Reminder
                Text(
                    text = reminder.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_padding)))
                // Due time of the reminder
                Text(
                    text = "${reminder.getRelativeDateString()}, ${reminder.createTimeString()}",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReminderCardPreview() {
    RemindMeTheme {
        SavedReminderCard(
            ReminderState(
                title = "Call Eugene",
                description = "Tell him to push the changes made to his branch",
                dueDate = LocalDate.now().plusDays(4)
            )
        )
    }
}