package com.example.armybazar.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.armybazar.di.myModules
import com.example.armybazar.model.Repository.TokenInMemory
import com.example.armybazar.model.Repository.User.UserRepository
import com.example.armybazar.ui.features.IntroScreen
import com.example.armybazar.ui.features.cart.CartScreen
import com.example.armybazar.ui.features.category.CategoryScreen
import com.example.armybazar.ui.features.main.MainScreen
import com.example.armybazar.ui.features.product.ProductScreen
import com.example.armybazar.ui.features.profile.ProfileScreen
import com.example.armybazar.ui.features.signIn.SignInScreen
import com.example.armybazar.ui.features.signup.SignUpScreen
import com.example.armybazar.ui.theme.MainAppThem
import com.example.armybazar.util.KEY_CATEGORY_ARG
import com.example.armybazar.util.KEY_PRODUCT_ARG
import com.example.armybazar.util.MyScreen
import org.koin.android.ext.koin.androidContext
import org.koin.compose.koinInject
import org.koin.dsl.koinApplication


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContent {
            koinApplication {
                androidContext(this@MainActivity)
                modules(myModules)
            }
            MainAppThem {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()

                ) {
                    val userRepository: UserRepository = koinInject()  //== get()
                    userRepository.loadToken()

                    ArmybazarUi()
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()

    ) {
        ArmybazarUi()
    }
}


@Composable
fun ArmybazarUi(navcontroller: NavHostController = rememberNavController()) {


    NavHost(
        navController = navcontroller,
        startDestination = MyScreen.MainScreen.route
    ) {
        composable(MyScreen.MainScreen.route) {
            if (TokenInMemory.token != null) {
                MainScreen(navcontroller)
            }else{
                IntroScreen(navcontroller)
            }
        }

        composable(
            MyScreen.ProductScreen.route + "/{$KEY_PRODUCT_ARG}",
            arguments = listOf(navArgument(KEY_PRODUCT_ARG) {
                type = NavType.StringType
            })
        ) {
            ProductScreen(
                navigation = navcontroller,
                productId = it.arguments!!.getString(KEY_PRODUCT_ARG, "null")
            )
        }
        composable(
            MyScreen.CategoryScreen.route + "/{$KEY_CATEGORY_ARG}",
            arguments = listOf(navArgument(KEY_CATEGORY_ARG) {
                type = NavType.StringType
            })
        ) {
            CategoryScreen(it.arguments!!.getString(KEY_CATEGORY_ARG, "null"), navcontroller)
        }
        composable(MyScreen.ProfileScreen.route) {
            ProfileScreen(navcontroller)
        }
        composable(MyScreen.CartScreen.route) {
            CartScreen(navcontroller)
        }
        composable(MyScreen.SingUpScreen.route) {
            SignUpScreen(navcontroller)
        }
        composable(MyScreen.SignInScreen.route) {
            SignInScreen(navcontroller)
        }
        composable(MyScreen.IntroScreen.route) {
            IntroScreen(navController = navcontroller)
        }

    }

}

