package com.example.armybazar.ui.features.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.armybazar.R
import com.example.armybazar.ui.features.product.MainTextField
import com.example.armybazar.ui.theme.Blue
import com.example.armybazar.ui.theme.Shapes
import com.example.armybazar.util.MyScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navigation: NavController) {
    val context = LocalContext.current
    val viewModel = koinViewModel<ProfileViewModel>()
    viewModel.loadUserData()

    Box {
        //part1

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ProfileToolbar() {
                navigation.popBackStack()
            }

            MainAnimation()

            Spacer(modifier = Modifier.padding(top = 6.dp))

            ShowDataSection("Email Address", viewModel.email.value, null)
            ShowDataSection("Address", viewModel.address.value) {
                viewModel.showLocationDialog.value = true
            }
            ShowDataSection("Postal Code", viewModel.postalCode.value) {
                viewModel.showLocationDialog.value = true
            }
            ShowDataSection("Login Time", viewModel.loginTime.value, null)

            Button(
                onClick = {
                    Toast.makeText(context, "Hope To See You Again :) ", Toast.LENGTH_SHORT).show()
                    viewModel.SignOut()
                    navigation.navigate(MyScreen.IntroScreen.route) {
                        popUpTo(MyScreen.IntroScreen.route) {
                            inclusive = true
                        }
                        navigation.popBackStack()
                        navigation.popBackStack()
                    }

                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 36.dp)
            ) {
                Text(text = "Sign Out")
            }

        }
        // part2
        if(viewModel.showLocationDialog.value) {

            AddUserLocationDataDialog(
                showSaveLocation = false ,
                onDismiss = { viewModel.showLocationDialog.value = false } ,
                onSubmitClicked = { address , postalCode , _ ->
                    viewModel.setUserLocation(address , postalCode)

                }
            )

        }

    }
}

//----------------------------------------------

@Composable
fun AddUserLocationDataDialog(
    showSaveLocation: Boolean,
    onDismiss: () -> Unit,
    onSubmitClicked: (String, String, Boolean) -> Unit
) {

    val context = LocalContext.current
    val checkedState = remember { mutableStateOf(false) }
    val userAddress = remember { mutableStateOf("") }
    val userPostalCode = remember { mutableStateOf("") }


    Dialog(onDismissRequest = onDismiss) {

        Card(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = Shapes.medium
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Add Location Data",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))

                MainTextField(userAddress.value, "your address...") {
                    userAddress.value = it
                }

                MainTextField(userPostalCode.value, "your postal code...") {
                    userPostalCode.value = it
                }

                if (showSaveLocation) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                        )

                        Text(text = "Save To Profile")

                    }

                }


                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {

                        if (
                            (userAddress.value.isNotEmpty() || userAddress.value.isNotBlank()) &&
                            (userPostalCode.value.isNotEmpty() || userPostalCode.value.isNotBlank())
                        ) {
                            onSubmitClicked(
                                userAddress.value,
                                userPostalCode.value,
                                checkedState.value
                            )
                            onDismiss.invoke()
                        } else {
                            Toast.makeText(context, "please write first...", Toast.LENGTH_SHORT)
                                .show()
                        }


                    }) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}


//-------------------------------------------------
@Composable
fun MainAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.profile_anim)
    )
    LottieAnimation(
        modifier = Modifier
            .size(270.dp)
            .padding(top = 36.dp, bottom = 16.dp),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

//-----------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileToolbar(
    OnBackClick: () -> Unit
) {
    TopAppBar(

        navigationIcon = {

            IconButton(onClick = { OnBackClick.invoke() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }

        },

        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "My Profile",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 58.dp)
            )
        }

    )


}

//-------------------------------------------------------

@Composable
fun ShowDataSection(
    subject: String,
    textToShow: String,
    OnLocationClicked: (() -> Unit)?
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .clickable { OnLocationClicked?.invoke() },
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = subject,
            style = TextStyle(fontSize = 18.sp, color = Blue, fontWeight = FontWeight.Bold)
        )

        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = textToShow,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
        Divider(color = Blue, thickness = 0.5.dp, modifier = Modifier.padding(top = 16.dp))
    }
}

