package com.example.remindme.reminder.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.remindme.R
import com.example.remindme.reminder.data.NewReminderViewModel
import com.example.remindme.ui.theme.RemindMeTheme

/**
 * Displays existing reminders
 */
@Composable
fun SavedRemindersScreen(
    viewModel: NewReminderViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.retrieveSavedReminders()
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.medium_padding))
        ) {
            items(viewModel.savedReminders) {
                SavedReminderCard(it)
            }
        }
        if(viewModel.isLoadingContent) {
            CircularProgressIndicator()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SavedRemindersScreenPreview() {
    RemindMeTheme(darkTheme = false) {
//        SavedRemindersScreen()
    }
}