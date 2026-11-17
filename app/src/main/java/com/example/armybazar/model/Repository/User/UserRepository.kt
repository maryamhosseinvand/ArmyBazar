package com.example.armybazar.model.Repository.User

interface UserRepository {

    //online
    suspend fun Signup(name: String , username:String , password:String):String
    suspend fun SignIn(username:String , password:String):String

    //offline
    fun SignOut()
    fun loadToken()

    fun SaveToken(newToken: String)
    fun getToken():String?

    fun saveUserName(userName:String)
    fun getUserName():String?

    fun saveUserLocation(address: String , postalCode : String)
    fun getUserLocation(): Pair<String , String>

    fun saveUserLoginTime()
    fun getUserLoginTime(): String
}