package com.example.armybazar.ui.features.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.armybazar.R
import com.example.armybazar.model.data.Ads
import com.example.armybazar.model.data.CheckOut
import com.example.armybazar.model.data.Product
import com.example.armybazar.ui.theme.Blue
import com.example.armybazar.ui.theme.CardViewBackground
import com.example.armybazar.ui.theme.MainAppThem
import com.example.armybazar.ui.theme.Shapes
import com.example.armybazar.util.CATEGORY
import com.example.armybazar.util.MyScreen
import com.example.armybazar.util.NO_PAYMENT
import com.example.armybazar.util.NetworkChecker
import com.example.armybazar.util.PAYMENT_PENDING
import com.example.armybazar.util.PAYMENT_SUCCESS
import com.example.armybazar.util.TAGS
import com.example.armybazar.util.stylePrice
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainAppThem {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background

        ) {
            MainScreen(rememberNavController())
        }
    }
}


@Composable
fun MainScreen(navigation: NavController) {
    val context = LocalContext.current
    val uiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.surface
    SideEffect { uiController.setStatusBarColor(statusBarColor) }
    val viewmodel = koinViewModel<MainScreenViewModel>(
        parameters = { parametersOf(NetworkChecker(context).isInternetConnected) }

    )
    if (NetworkChecker(context).isInternetConnected) {
        viewmodel.loadBadgeNumber()
    }

    if (viewmodel.getPaymentStatus() == PAYMENT_PENDING) {
        if (NetworkChecker(context).isInternetConnected) {
            viewmodel.getCheckoutData()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            TopToolbar(
                badgNumber = viewmodel.badgeNumber.value ,
                onCartClicked = {
                    if (NetworkChecker(context).isInternetConnected)
                        navigation.navigate(MyScreen.CartScreen.route)
                    else
                        Toast.makeText(context, "Please Connect Internet...", Toast.LENGTH_SHORT)
                            .show()
                },
                onProfileCliked = { navigation.navigate(MyScreen.ProfileScreen.route) },

                )


            CategoryBar(CATEGORY) { category ->
                navigation.navigate(MyScreen.CategoryScreen.route + "/$category")
            }

            val productDataState = viewmodel.dataProducts
            val adsDataState = viewmodel.dataAds
            ProductSubjectList(TAGS, productDataState.value, adsDataState.value) { productId ->
                navigation.navigate(MyScreen.ProductScreen.route + "/$productId")
            }
        }

        if (viewmodel.showProgresBar.value) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .align(Alignment.TopCenter),
                color = Blue
            )
        }
        if (viewmodel.showPaymentResultDialog.value){
            PaymentResultDialog(
                viewmodel.checkoutData.value,
                onDismiss ={
                    viewmodel.showPaymentResultDialog.value=false
                    viewmodel.setPaymentStatus(NO_PAYMENT)
                }
            )

        }
    }
}

//------------------------------------------------------

@Composable
private fun PaymentResultDialog(
    checkoutResult: CheckOut,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {

        Card(
            shape = Shapes.medium
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Payment Result",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Main Data
                if (checkoutResult.order?.status?.toInt() == PAYMENT_SUCCESS) {

                    AsyncImage(
                        model = R.drawable.success_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(110.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Payment was successful!",
                        style = TextStyle(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Purchase Amount: " + stylePrice(
                            (checkoutResult.order.amount).substring(
                                0,
                                (checkoutResult.order.amount).length - 1
                            )
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                } else {

                    AsyncImage(
                        model = R.drawable.fail_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(top = 6.dp, bottom = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Payment was not successful!",
                        style = TextStyle(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Purchase Amount: " + stylePrice(
                            (checkoutResult.order!!.amount).substring(
                                0,
                                (checkoutResult.order.amount).length - 1
                            )
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }

                // Ok Button
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "ok")
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                }
            }
        }
    }
}



//-------------------------------------------------------------------------------------
@Composable
fun ProductSubjectList(
    tags: List<String>,
    products: List<Product>,
    ads: List<Ads>,
    onProductClicked: (String) -> Unit
) {

    if (products.isNotEmpty()) {
        Column {
            tags.forEachIndexed { it, _ ->
                val withTagData = products.filter { product -> product.tags == tags[it] }
                ProductSubject(
                    tags[it], withTagData.shuffled(),
                    onProductClicked
                )

                if (ads.size >= 2)
                    if (it == 1 || it == 2)
                        BigPictureTablighat(ads[it - 1], onProductClicked)
            }

        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolbar(
    badgNumber: Int,
    onCartClicked: () -> Unit,
    onProfileCliked: () -> Unit
) {
    TopAppBar(

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        title = {
            Text(
                text = "ArmyBazar",
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        actions = {
            Box {
                IconButton(onClick = { onCartClicked.invoke() }) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (badgNumber != 0) {
                    Badge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-4).dp, y = 4.dp)
                    ) {
                        Text(badgNumber.toString())
                    }
                }
            }

            IconButton(onClick = { onProfileCliked.invoke() }) {
                Icon(
                    Icons.Default.Person,
                    null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

        }
    )

}


//--------------------------------------------------------------------


@Composable
fun CategoryBar(categoryList: List<Pair<String, Int>>, onCategoryClicked: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(end = 16.dp)
    )
    {
        items(categoryList.size) {
            CategoryItem(categoryList[it], onCategoryClicked)
        }
    }

}

@Composable
fun CategoryItem(Subject: Pair<String, Int>, onCategoryClicked: (String) -> Unit) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onCategoryClicked.invoke(Subject.first) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = Shapes.medium,
            color = CardViewBackground
        ) {
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp),
                painter = painterResource(id = Subject.second),
                contentDescription = null
            )
        }
        Text(
            text = Subject.first,
            modifier = Modifier.padding(top = 4.dp),
            style = TextStyle(color = MaterialTheme.colorScheme.onSurface)
        )

    }

}


//--------------------------------------------------------------------


@Composable
fun ProductSubject(subject: String, data: List<Product>, onProductClicked: (String) -> Unit) {

    Column {
        Text(
            modifier = Modifier.padding(15.dp),
            text = subject,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp
            )
        )

        ProductBar(data, onProductClicked)

    }
}

@Composable
fun ProductBar(data: List<Product>, onProductClicked: (String) -> Unit) {

    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(data.size) {
            ProductItem(data[it], onProductClicked)
        }
    }

}

@Composable
fun ProductItem(product: Product, onProductClicked: (String) -> Unit) {

    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onProductClicked.invoke(product.productId) },
        elevation = CardDefaults.cardElevation(16.dp),
        shape = Shapes.large

    ) {
        Column {

        }
        AsyncImage(
            model = product.imgUrl,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = product.name,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stylePrice(product.price),
                style = TextStyle(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = product.soldItem + "Sold",
                style = TextStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 14.sp)
            )

        }


    }

}


//--------------------------------------------------------------------

@Composable
fun BigPictureTablighat(ads: Ads, onProductClicked: (String) -> Unit) {

    AsyncImage(
        model = ads.imageURL,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(top = 23.dp, start = 16.dp, end = 16.dp)
            .clip(Shapes.medium)
            .clickable { onProductClicked.invoke(ads.productId) },
        contentDescription = null,
        contentScale = ContentScale.Crop


    )

}


//--------------------------------------------------------------------