package com.example.newsapp.screens.loginRegisterScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.repository.firebaseRepo.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    var errorMessage by mutableStateOf("")
    var loginSuccess by mutableStateOf(false)
    var registrationSuccess by mutableStateOf(false)

    var isLoadingLogin by mutableStateOf(false)
        private set

    var isLoadingRegister by mutableStateOf(false)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoadingLogin = true
            val result = firebaseRepository.login(email, password)
            if (result.isSuccess) {
                loginSuccess = true
            } else {
                errorMessage = when (result.exceptionOrNull()?.message) {
                    "The email address is badly formatted." -> "Invalid email"
                    "The supplied auth credential is incorrect, malformed or has expired." -> "Invalid password"
                    else -> "An error occurred"
                }
            }
            isLoadingLogin = false
        }
    }

    fun register(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            isLoadingRegister = true
            val result = firebaseRepository.register(email, password, firstName, lastName)
            if (result.isSuccess) {
                registrationSuccess = true
            } else {
                errorMessage = when (result.exceptionOrNull()?.message) {
                    "The email address is badly formatted." -> "Invalid email"
                    "The email address is already in use by another account" -> "he email address is already in use by another account"
                    "The supplied auth credential is incorrect, malformed or has expired." -> "Invalid password"
                    else -> "An error occurred"
                }
            }
            isLoadingRegister = false
        }
    }
}