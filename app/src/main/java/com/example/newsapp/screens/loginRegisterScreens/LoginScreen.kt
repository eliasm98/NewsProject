package com.example.newsapp.screens.loginRegisterScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newsapp.data.Screens

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    val viewModel: LoginRegisterViewModel = hiltViewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.loginSuccess) {
        if (viewModel.loginSuccess) {
            navController.navigate(Screens.Home.name) {
                popUpTo("Login") { inclusive = true }
            }
        }
    }

    if (viewModel.errorMessage.isNotBlank()) {
        AlertDialog(
            onDismissRequest = { viewModel.errorMessage = "" },
            title = { Text("Login Failed") },
            text = { Text(viewModel.errorMessage) },
            confirmButton = {
                Button(onClick = { viewModel.errorMessage = "" }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { viewModel.login(email, password) }) { Text("Login") }
        Button(onClick = { navController.navigate("register") }) { Text("Register") }
    }
}