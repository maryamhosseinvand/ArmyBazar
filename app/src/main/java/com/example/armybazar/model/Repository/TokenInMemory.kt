package com.example.armybazar.model.Repository

object TokenInMemory {

    var username: String? = null
        private set
    var token: String? = null
       private set

    fun refreshToken(username: String?, newtoken: String?) {
        this.username = username
        this.token = newtoken

    }

}
