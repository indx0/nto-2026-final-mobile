package ru.myitschool.work.ui.screen.room

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun MeetingRoomScreen(
    viewModel: MeetingRoomActionViewModel = viewModel(),
    navController: NavController
){
    val roomDataState by viewModel.roomDataState.collectAsState()
    val roomScheduleState by viewModel.roomDataState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is MeetingRoomAction.Open -> {
                    navController.navigate(action.destination) {
                        if (action.clearBackStack) {
                            popUpTo(0)
                        }
                    }
                }
            }
        }
    }
}