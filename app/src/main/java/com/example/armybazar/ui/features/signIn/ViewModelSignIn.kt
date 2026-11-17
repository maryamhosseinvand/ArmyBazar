package com.example.armybazar.ui.features.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.armybazar.model.Repository.User.UserRepository
import com.example.armybazar.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")

    fun signInUser(LoggingEvent: (String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {
            val result = userRepository.SignIn(email.value!!, password.value!!)
            LoggingEvent(result)
        }

    }

}