package com.example.armybazar.ui.features.signup

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.armybazar.R
import com.example.armybazar.ui.theme.BackgroundMain
import com.example.armybazar.ui.theme.Blue
import com.example.armybazar.ui.theme.MainAppThem
import com.example.armybazar.util.MyScreen
import com.example.armybazar.util.NetworkChecker
import com.example.armybazar.util.VALUE_SUCCESS
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {


    MainAppThem {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()

        ) {
            SignUpScreen(rememberNavController())

        }
    }

}


@Composable
fun SignUpScreen(navigation: NavController) {

    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Blue)
    }

    val viewmodel = koinViewModel<SignUpViewModel>()
    val context = LocalContext.current

    ClearInputs(viewmodel)

    Box {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.47f)
                .background(Blue)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconApp()
            MainCardView(navigation, viewmodel){
                viewmodel.signUpUser {
                    if (it== VALUE_SUCCESS){
                        navigation.navigate(MyScreen.MainScreen.route){
                         popUpTo(MyScreen.IntroScreen.route){
                             inclusive= true
                         }
                        }
                    }else{
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }
    }

}

@Composable
fun IconApp() {

    Surface(
        modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
    ) {
        Image(
            modifier = Modifier.padding(20.dp),
            painter = painterResource(R.drawable.ic_icon_app),
            contentDescription = null
        )
    }

}


@Composable
fun MainCardView(
    navigation: NavController,
    viewModelSignUp: SignUpViewModel,
    SignUpEvent: () -> Unit,

) {

    val name = viewModelSignUp.namee.observeAsState("")
    val email = viewModelSignUp.email.observeAsState("")
    val password = viewModelSignUp.password.observeAsState("")
    val confrimPassword = viewModelSignUp.confirmPassword.observeAsState("")
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            Text(
                text = "SignUP",
                style = TextStyle(color = Blue, fontSize = 28.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp)
            )

            MainTextField(
                name.value,
                hint = "Full Your Name",
                R.drawable.ic_person
            ) { viewModelSignUp.namee.value = it }
            MainTextField(
                email.value,
                hint = "Email",
                R.drawable.ic_email
            ) { viewModelSignUp.email.value = it }
            PasswordTextField(
                password.value,
                hint = "Password",
                R.drawable.ic_password
            ) { viewModelSignUp.password.value = it }
            PasswordTextField(
                confrimPassword.value,
                hint = "ConfrimPassword",
                R.drawable.ic_password
            ) { viewModelSignUp.confirmPassword.value = it }

            Button(
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp),
                onClick = {


                    if (name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty() && confrimPassword.value.isNotEmpty()) {
                        if (password.value.length >= 8) {
                            if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                                if (NetworkChecker(context).isInternetConnected) {
                                    if(password.value==confrimPassword.value){
                                        SignUpEvent.invoke()

                                    }else{Toast.makeText(
                                        context,
                                        "Password Are Not The Same!",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                    }

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please Connect to the Internet",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }


                            } else {
                                Toast.makeText(
                                    context,
                                    "Email Format is Not True",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


                        } else {
                            Toast.makeText(context, "Password Characters Should Be More Than 8!", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(context, "Please Fill Data First...", Toast.LENGTH_SHORT)
                            .show()
                    }


                }


            ) {

                Text(
                    text = "Register Account",
                    modifier = Modifier.padding(8.dp)
                )
            }

            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Already Have An Account?")

                TextButton(
                    onClick = {
                        navigation.navigate(MyScreen.SignInScreen.route) {
                            popUpTo(MyScreen.SingUpScreen.route) {
                                inclusive = true
                            }
                        }

                    }
                ) {
                    Text(text = "LogIn", color = Blue)
                }


            }


        }


    }


}


@Composable
fun MainTextField(
    edtvalue: String,
    hint: String,
    icon: Int,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType= KeyboardType.Email),
        shape = RoundedCornerShape(16.dp),
        value = edtvalue,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(12.dp),
        label = { Text(hint) },
        placeholder = { Text(hint) },
        singleLine = true,
        onValueChange = onValueChange,
        leadingIcon = { Icon(painterResource(icon), null) }


    )
}


@Composable
fun PasswordTextField(
    edtvalue: String,
    hint: String,
    icon: Int,
    onValueChange: (String) -> Unit
) {
    val PasswordVisible = remember { mutableStateOf(false) }
    OutlinedTextField(
        shape = RoundedCornerShape(16.dp),
        value = edtvalue,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(12.dp),
        label = { Text(hint) },
        placeholder = { Text(hint) },
        singleLine = true,
        onValueChange = onValueChange,
        leadingIcon = { Icon(painterResource(icon), null) },
        visualTransformation = if (PasswordVisible.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
        ),
        trailingIcon = {
            val image = if (PasswordVisible.value)
                painterResource(R.drawable.ic_invisible)
            else
                painterResource(R.drawable.ic_visible)
            Icon(
                painter = image,
                contentDescription = null,
                modifier = Modifier.clickable { PasswordVisible.value = !PasswordVisible.value }
            )

        }


    )
}

fun ClearInputs(viewModel: SignUpViewModel)
{
    viewModel.namee.value =""
    viewModel.email.value=""
    viewModel.password.value=""
    viewModel.confirmPassword.value=""
}
