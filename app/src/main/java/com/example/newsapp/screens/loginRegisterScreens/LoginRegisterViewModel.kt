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


    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = firebaseRepository.login(email, password)
            if(result.isSuccess) {
                loginSuccess = true
            }

        }
    }

    fun register(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            val result = firebaseRepository.register(email, password, firstName, lastName)
            if(result.isSuccess){
                registrationSuccess = true
            }
        }
    }
}