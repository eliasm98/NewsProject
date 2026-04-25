package com.example.newsapp.screens.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.firebase.FirestoreUser
import com.example.newsapp.repository.firebaseRepo.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _user = MutableStateFlow<FirestoreUser?>(null)
    val user: StateFlow<FirestoreUser?> = _user

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = firebaseRepository.getUser()
            _user.value = user
            _firstName.value = user?.firstName ?: ""
            _lastName.value = user?.lastName ?: ""
        }
    }

    fun onFirstNameChange(value: String) { _firstName.value = value }
    fun onLastNameChange(value: String) { _lastName.value = value }

    fun updateUser() {
        viewModelScope.launch {
            val updatedUser = _user.value?.copy(
                firstName = _firstName.value,
                lastName = _lastName.value
            ) ?: return@launch

            firebaseRepository.updateUser(updatedUser)
            _updateSuccess.value = true
        }
    }
}