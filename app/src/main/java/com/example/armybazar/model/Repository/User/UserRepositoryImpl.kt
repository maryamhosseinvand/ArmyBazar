package com.example.armybazar.model.Repository.User

import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import com.example.armybazar.model.Repository.TokenInMemory
import com.example.armybazar.model.net.ApiService
import com.example.armybazar.util.VALUE_SUCCESS
import com.google.gson.JsonObject
import java.util.Date
import java.util.Locale

class UserRepositoryImpl(
    private val apiservice: ApiService, private val sherpref: SharedPreferences

) : UserRepository {
    override suspend fun Signup(name: String, username: String, password: String):String {


        val jsonObject = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", username)
            addProperty("password", password)


        }

          val result= apiservice.signUp(jsonObject= jsonObject)
        if(result.success){

            TokenInMemory.refreshToken(username , result.token)
            SaveToken(result.token)
            saveUserName(username)
            saveUserLoginTime()

        return VALUE_SUCCESS
        }else {
           return result.message
        }

    }

    override suspend fun SignIn(username: String, password: String):String {

        val jsonObject = JsonObject().apply {
            addProperty("email" ,username)
            addProperty("password", password)

        }

         val result = apiservice.signIn(jsonObject= jsonObject)
          if (result.success){
              TokenInMemory.refreshToken(username , result.token)
              SaveToken(result.token)
              saveUserName(username)
              saveUserLoginTime()
              return VALUE_SUCCESS
          }else{
              return result.message
          }

    }

    override fun SignOut() {
        TokenInMemory.refreshToken(null , null )
        sherpref.edit { clear() }
    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getUserName() ,getToken())
    }

    override fun SaveToken(newToken: String) {
       sherpref.edit { putString("token", newToken) }
    }

    override fun getToken(): String? {
        return sherpref.getString("token", null)
    }

    override fun saveUserName(userName: String) {
        sherpref.edit { putString("username", userName) }
    }

    override fun getUserName(): String? {
        return sherpref.getString("username", null)
    }

    override fun saveUserLocation(address: String, postalCode: String) {
         sherpref.edit { putString("address", address) }
        sherpref.edit { putString("postalCode", postalCode) }
    }

    override fun getUserLocation(): Pair<String, String> {
        val address = sherpref.getString("address", "click to add")!!
        val postalCode = sherpref.getString("postalCode" , "click to add")!!
        return Pair(address , postalCode)
    }

    override fun saveUserLoginTime() {
        val now = System.currentTimeMillis()
        sherpref.edit { putString("login-Time", now.toString()) }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getUserLoginTime(): String {
        val millis = sherpref.getString("login-Time", "0")!!.toLong()
        if (millis == 0L) return "ورودی ثبت نشده"

        val sdf = SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}