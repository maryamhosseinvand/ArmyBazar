package com.example.armybazar.ui.features.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.armybazar.model.data.Product
import com.example.armybazar.ui.theme.Shapes
import com.example.armybazar.util.MyScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategoryScreen(categoryName: String, navigation: NavController) {

    val viewModel = koinViewModel<CategoryScreenViewModel>()
    viewModel.loadDataByCategory(categoryName)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()

    ) {
        CategoryToolbar(categoryName)
        val data = viewModel.dataProducts
        CategoryList(data.value) { productId ->
            navigation.navigate(MyScreen.ProductScreen.route + "/$productId")

        }
    }


}

@Composable
fun CategoryItem(data: Product, onProductClicked: (String) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            .clickable { onProductClicked.invoke(data.productId) },
        elevation = CardDefaults.cardElevation(16.dp),
        shape = Shapes.large
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
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
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = data.name,
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = data.price + "Tomans",
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 8.dp, end = 8.dp)
                    .clip(Shapes.large)

            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = data.soldItem + "Sold",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                )
            }
        }
    }
}
//------------------------------------------------------------------------------

@Composable
fun CategoryList(data: List<Product>, onProductClicked: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(data.size) {
            CategoryItem(data[it], onProductClicked)
        }
    }
}

//------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryToolbar(categoryName: String) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = categoryName,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface

            )
        }
    )
}