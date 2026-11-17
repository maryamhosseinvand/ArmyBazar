package com.example.armybazar.ui.features.cart

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.armybazar.R
import com.example.armybazar.model.data.Product
import com.example.armybazar.ui.features.profile.AddUserLocationDataDialog
import com.example.armybazar.ui.theme.Blue
import com.example.armybazar.ui.theme.PriceBackground
import com.example.armybazar.ui.theme.Shapes
import com.example.armybazar.util.MyScreen
import com.example.armybazar.util.NetworkChecker
import com.example.armybazar.util.PAYMENT_PENDING
import com.example.armybazar.util.stylePrice
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreen(navigation: NavController) {

    val context = LocalContext.current
    val getDataDialogState = remember { mutableStateOf(false) }
    val viewModel = koinViewModel<CartViewModel>()
    viewModel.loadCartData()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(bottom = 74.dp)
        ) {

            CartToolBar(
                onBackedClicked = {
                    navigation.popBackStack()
                },
                OnProfileClicked = {
                    navigation.navigate(MyScreen.ProfileScreen.route)
                }
            )

            if (viewModel.productList.value.isNotEmpty()) {

                CartList(
                    data = viewModel.productList.value,
                    isChangingNumber = viewModel.isChangingNumber.value,
                    OnAddItemClicked = { viewModel.addItem(it) },
                    OnRemoveItemClicked = { viewModel.removeItem(it) },
                    OnItemClicked = { navigation.navigate(MyScreen.ProductScreen.route + "/" + it) }
                )

            } else {
                NoDataAnimation()
            }

        }



        PurchaseAll(
            totalprice = viewModel.totalprice.value.toString()

        ) {

            if (viewModel.productList.value.isNotEmpty()) {

                val locationData = viewModel.getUserLocation()
                if (locationData.first == "click to add" || locationData.second == "click to add") {
                    getDataDialogState.value = true
                } else {


                    viewModel.purchaseAll(
                        locationData.first,
                        locationData.second
                    ) { success, link ->

                        if (success) {

                            Toast.makeText(context, "Pay using zarinPal...", Toast.LENGTH_SHORT)
                                .show()

                            viewModel.setPaymentStatus(PAYMENT_PENDING)

                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            context.startActivity(intent)

                        } else {
                            Toast.makeText(context, "problem in payment...", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                }


            } else {
                Toast.makeText(context, "please add some products first...", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        if (getDataDialogState.value) {

            AddUserLocationDataDialog(
                showSaveLocation = true,
                onDismiss = { getDataDialogState.value = false },
                onSubmitClicked = { address, postalCode, isChecked ->

                    if (NetworkChecker(context).isInternetConnected) {

                        if (isChecked) {
                            viewModel.setUserLocation(address, postalCode)
                        }

                        // pardakht ->
                        viewModel.purchaseAll(address, postalCode) { success, link ->

                            if (success) {

                                Toast.makeText(context, "Pay using zarinPal...", Toast.LENGTH_SHORT)
                                    .show()

                                viewModel.setPaymentStatus(PAYMENT_PENDING)

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                context.startActivity(intent)

                            } else {
                                Toast.makeText(context, "problem in payment...", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }


                    } else {
                        Toast.makeText(
                            context,
                            "please connect to internet first...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
}



//------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartToolBar(
    onBackedClicked: () -> Unit,
    OnProfileClicked: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            IconButton(onClick = { onBackedClicked.invoke() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                text = "My Cart",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        actions = {
            IconButton(
                modifier = Modifier.padding(end = 6.dp),
                onClick = { OnProfileClicked.invoke() }
            ) {
                Icon(
                    Icons.Default.Person,
                    null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }


        }


    )
}

//-----------------------------------------------------
@Composable
fun CartList(
    data: List<Product>,
    isChangingNumber: Pair<String, Boolean>,
    OnAddItemClicked: (String) -> Unit,
    OnRemoveItemClicked: (String) -> Unit,
    OnItemClicked: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {

        items(data.size) {

            CartItem(
                data = data[it],
                isChangingNumber = isChangingNumber,
                OnAddItemClicked = OnAddItemClicked,
                OnRemoveItemClicked = OnRemoveItemClicked,
                OnItemClicked = OnItemClicked
            )
        }
    }

}

//-----------------------------------------------------------
@Composable
fun CartItem(
    data: Product,
    isChangingNumber: Pair<String, Boolean>,
    OnAddItemClicked: (String) -> Unit,
    OnRemoveItemClicked: (String) -> Unit,
    OnItemClicked: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { OnItemClicked.invoke(data.productId) },
        shape = Shapes.large
    ) {

        Column {

            AsyncImage(
                model = data.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.padding(10.dp)
                ) {

                    Text(
                        text = data.name,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "From " + data.category + " Group",
                        style = TextStyle(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = "Product authenticity guarantee",
                        style = TextStyle(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Available in stock to ship",
                        style = TextStyle(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )


                    Surface(
                        modifier = Modifier
                            .padding(top = 18.dp, bottom = 6.dp)
                            .clip(Shapes.large),
                        color = PriceBackground
                    ) {

                        Text(
                            modifier = Modifier.padding(
                                top = 6.dp,
                                bottom = 6.dp,
                                start = 8.dp,
                                end = 8.dp
                            ),
                            text = stylePrice(
                                (data.price.toInt() * (data.quantity ?: "1").toInt()).toString()
                            ),
                            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .padding(bottom = 14.dp, end = 8.dp)
                        .align(Alignment.Bottom),
                    color = MaterialTheme.colorScheme.surface
                ) {


                    Card(
                        border = BorderStroke(2.dp, Blue)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            if (data.quantity?.toInt() == 1) {

                                IconButton(onClick = { OnRemoveItemClicked.invoke(data.productId) }) {
                                    Icon(
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                            } else {

                                IconButton(onClick = { OnRemoveItemClicked.invoke(data.productId) }) {
                                    Icon(
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                        painter = painterResource(R.drawable.ic_minus),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                            }


                            // size of product
                            if (isChangingNumber.first == data.productId && isChangingNumber.second) {

                                Text(
                                    text = "...",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 12.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                            } else {

                                Text(
                                    text = data.quantity ?: "1",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                            }


                            // add button +
                            IconButton(onClick = { OnAddItemClicked.invoke(data.productId) }) {
                                Icon(
                                    modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
//------------------------------------------------------------------------------

@Composable
fun NoDataAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}

//-------------------------------------------------------------------
@Composable
fun PurchaseAll(
    totalprice: String,
    OnPurchaseClicked: () -> Unit
) {
    val context = LocalContext.current

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .height(40.dp)
                    .wrapContentWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                onClick = {
                    if (NetworkChecker(context).isInternetConnected) {
                        OnPurchaseClicked.invoke()
                    } else {
                        Toast.makeText(context, "please connect to internet...", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            ) {

                Text(
                    text = "Let's Purchase!",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                    color = androidx.compose.ui.graphics.Color.White
                )
            }

            Surface(
                modifier = Modifier.clip(Shapes.large),
                color = PriceBackground
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 6.dp,
                            bottom = 6.dp
                        ),
                    text = "total" + stylePrice(totalprice),
                    style = TextStyle(fontSize = 14.sp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}