package ru.myitschool.work.ui.screen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.GetTokenUseCase
import ru.myitschool.work.ui.nav.AppDestination
import ru.myitschool.work.ui.nav.AuthScreenDestination
import ru.myitschool.work.ui.nav.BookScreenDestination
import ru.myitschool.work.ui.nav.MainScreenDestination
import ru.myitschool.work.ui.nav.MeetingRoomScreenDestination
import ru.myitschool.work.ui.screen.auth.AuthScreen
import ru.myitschool.work.ui.screen.auth.AuthViewModel
import ru.myitschool.work.ui.screen.book.BookScreen
import ru.myitschool.work.ui.screen.main.MainScreen
import ru.myitschool.work.ui.screen.room.MeetingRoomScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel = viewModel()
) {
    val role by viewModel.role.collectAsStateWithLifecycle()
    var destination by remember { mutableStateOf<AppDestination?>(null) }
    LaunchedEffect(role) {
        val token = GetTokenUseCase(
            repository = AuthRepository()
        ).invoke()
        destination = if (token == null || role == null) {
            AuthScreenDestination    //TODO REPLACE ON AuthScreenDestination
        } else {
            if(role != "MEETING_ROOM") MainScreenDestination else MeetingRoomScreenDestination
        }
    }
    if (destination != null) {
        NavHost(
            modifier = modifier,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            navController = navController,
            startDestination = destination as AppDestination,
        ) {
            composable<AuthScreenDestination> {
                AuthScreen(navController = navController)
            }
            composable<MeetingRoomScreenDestination> {
                MeetingRoomScreen(navController = navController)
            }
            composable<MainScreenDestination> {
                MainScreen(navController = navController)
            }
            composable<BookScreenDestination> {
                BookScreen(navController = navController)
            }
        }
    }
}