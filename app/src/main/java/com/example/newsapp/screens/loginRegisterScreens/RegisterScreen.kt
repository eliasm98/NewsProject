package com.example.newsapp.screens.loginRegisterScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newsapp.data.Screens
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val viewModel: LoginRegisterViewModel = hiltViewModel()

    LaunchedEffect(viewModel.registrationSuccess) {
        if (viewModel.registrationSuccess) {
            snackbarHostState.showSnackbar("User created successfully!")
            navController.navigate(Screens.Login.name)
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        if (viewModel.errorMessage.isNotBlank()) {
            snackbarHostState.showSnackbar(viewModel.errorMessage)
            viewModel.errorMessage = ""
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") }
            )
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") }
            )
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please fill in all fields!")
                        }
                    } else {
                        viewModel.register(email, password, firstName, lastName)
                    }
                }
            ) { Text("Register") }

            Button(
                onClick = { navController.navigate(Screens.Login.name) }
            ) { Text("Back") }
        }
    }
}