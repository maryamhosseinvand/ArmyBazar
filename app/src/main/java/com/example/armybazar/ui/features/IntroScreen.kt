package com.example.armybazar.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.armybazar.R
import com.example.armybazar.ui.theme.BackgroundMain
import com.example.armybazar.ui.theme.Blue
import com.example.armybazar.ui.theme.MainAppThem
import com.example.armybazar.util.MyScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    MainAppThem {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()

        ) {
            IntroScreen(navController = rememberNavController())

        }


    }
}

@Composable
fun IntroScreen(navController: NavController) {

    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Blue)
    }


    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.img_intro),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(top = 500.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            onClick = {
                navController.navigate(MyScreen.SingUpScreen.route)
            }
        ) {
            Text(text = "Sign Up")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(Color.White),
            onClick = {
                navController.navigate(MyScreen.SignInScreen.route)
            }
        ) {
            Text(
                text = "Sign In",
                color = Blue

            )
        }

    }

}
