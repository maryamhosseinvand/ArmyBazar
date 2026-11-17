package com.example.armybazar.ui.features.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.armybazar.model.Repository.User.UserRepository

class ProfileViewModel(
    private val UserRepository: UserRepository,
) : ViewModel() {

    val email = mutableStateOf("")
    val address =mutableStateOf("")
    val postalCode = mutableStateOf("")
    val loginTime = mutableStateOf("")

    val showLocationDialog = mutableStateOf(false)

    fun loadUserData(){

        email.value = UserRepository.getUserName()!!
        loginTime.value =UserRepository.getUserLoginTime()

        val location = UserRepository.getUserLocation()
        address.value = location.first
        postalCode.value =location.second


    }

    fun SignOut(){
        UserRepository.SignOut()
    }

    fun setUserLocation(address : String ,postalCode : String){
          UserRepository.saveUserLocation(address,postalCode)
    }

}