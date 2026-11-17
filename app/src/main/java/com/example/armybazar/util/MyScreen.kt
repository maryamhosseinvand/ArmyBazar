package com.example.armybazar.util

sealed class MyScreen(val route: String) {

    data object MainScreen : MyScreen("mainScreen")
    data object ProductScreen : MyScreen("productScreen")
    data object CategoryScreen : MyScreen("categoryScreen")
    data object ProfileScreen : MyScreen("profileScreen")
    data object CartScreen : MyScreen("cartScreen")
    data object SingUpScreen : MyScreen("singUpScreen")
    data object SignInScreen : MyScreen("signInScreen")
    data object IntroScreen : MyScreen("introScreen")



}