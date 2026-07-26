package ru.myitschool.work.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.myitschool.work.R
import ru.myitschool.work.core.TestIds
import ru.myitschool.work.ui.component.AppButton
import ru.myitschool.work.ui.component.InputField

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val configuratiom = LocalConfiguration.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is AuthAction.Open -> navController.navigate(action.destination) {
                    popUpTo(0)
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = if (configuratiom.screenHeightDp.dp < 600.dp) 16.dp else 84.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.auth_title),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(24.dp))

        when (val currentState = state) {
            is AuthState.Data -> Content(viewModel, currentState)
            is AuthState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}


@Composable
private fun Content(
    viewModel: AuthViewModel,
    state: AuthState.Data
) {
    var loginText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 512.dp)
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, top = 36.dp, end = 16.dp)
        ) {
            InputField(
                label = stringResource(R.string.auth_login_label),
                value = loginText,
                onValueChange = {
                    loginText = it
                    viewModel.onIntent(AuthIntent.TextInput(loginText, passwordText))
                },
                placeholder = stringResource(R.string.auth_login_placeholder)
            )

            Spacer(modifier = Modifier.size(16.dp))

            InputField(
                label = stringResource(R.string.auth_password_label),
                value = passwordText,
                onValueChange = {
                    passwordText = it
                    viewModel.onIntent(AuthIntent.TextInput(loginText, passwordText))
                },
                placeholder = stringResource(R.string.auth_password_placeholder)
            )

            Spacer(modifier = Modifier.size(36.dp))

            if (state.error != null) {
                var error = state.error
                when(error.trim()){
                    "401" -> error = stringResource(R.string.auth_unauthorised)
                    "Network is unreachable" -> error = stringResource(R.string.network_is_unreachable)
                    "Too many attempts" -> error = stringResource(R.string.auth_too_many_attempts)
                }
                Text(
                    modifier = Modifier
                        .testTag(TestIds.Auth.ERROR)
                        .fillMaxWidth(),
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(12.dp))
            }

            AppButton(
                onClick = { viewModel.onIntent(AuthIntent.Send(loginText, passwordText)) },
                enabled = state.isEnabledSend,
                text = stringResource(R.string.auth_sign_in),
                modifier = Modifier.testTag(TestIds.Auth.SIGN_BUTTON)
            )

            Spacer(modifier = Modifier.size(36.dp))
        }
    }
}