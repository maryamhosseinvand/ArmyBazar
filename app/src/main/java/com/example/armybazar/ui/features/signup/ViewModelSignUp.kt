package com.example.armybazar.ui.features.signup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.armybazar.model.Repository.User.UserRepository
import com.example.armybazar.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    val namee = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    fun signUpUser(LoggingEvent: (String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {
            val result = userRepository.Signup(namee.value!!, email.value!!, password.value!!)
            LoggingEvent(result)
        }

    }

}