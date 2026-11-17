package com.example.armybazar.ui.features.product

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.armybazar.R
import com.example.armybazar.model.data.Comment
import com.example.armybazar.model.data.Product
import com.example.armybazar.ui.theme.Blue
import com.example.armybazar.ui.theme.MainAppThem
import com.example.armybazar.ui.theme.Shapes
import com.example.armybazar.util.MyScreen
import com.example.armybazar.util.NetworkChecker
import com.example.armybazar.util.stylePrice
import org.koin.androidx.compose.koinViewModel


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ProductScreenPreview() {
    MainAppThem {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductScreen(rememberNavController(), productId = "")
        }
    }
}


@Composable
fun ProductScreen(navigation: NavController, productId: String) {
    val context = LocalContext.current
    val viewmodel = koinViewModel<ProductScreenViewModel>()
    val configuration = LocalConfiguration.current
    val cartHeightFraction =
        if (configuration.orientation ==
            android.content.res.Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.08f
    viewmodel.loadData(productId, NetworkChecker(context).isInternetConnected)



    Box(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = (LocalConfiguration.current.screenHeightDp.dp * cartHeightFraction))


        ) {
            ProductToolBar(
                productName = viewmodel.thisProduct.value.name,
                badgNumber = viewmodel.badgeNumber.value,
                onBackedClicked = { navigation.popBackStack() },
                onCartClicked = {

                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreen.CartScreen.route)

                    } else {
                        Toast.makeText(
                            context,
                            "Please Connected To Internet ...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
            val comments =
                if (NetworkChecker(context).isInternetConnected) viewmodel.comments.value else listOf()
            ProductItem(
                data = viewmodel.thisProduct.value,
                comments = comments,
                OnCategoryClicked =
                    { navigation.navigate(MyScreen.CategoryScreen.route + "/" + it) },
                OnAddNewComment = {
                    viewmodel.addNewComment(productId, it) { payam ->
                        Toast.makeText(context, payam, Toast.LENGTH_SHORT).show()
                    }
                }

            )
        }

        AddtoCart(
            viewmodel.thisProduct.value.price,
            viewmodel.isAddingProduct.value,
            cartHeightFraction

        ) {
            if (NetworkChecker(context).isInternetConnected) {

                viewmodel.addProductToCart(productId) {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Connect To Internet...", Toast.LENGTH_SHORT).show()
            }
        }


    }


}
//------------------------------------------------------


@Composable
fun ProductItem(
    data: Product,
    comments: List<Comment>,
    OnCategoryClicked: (String) -> Unit,
    OnAddNewComment: (String) -> Unit
) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        ProductDesign(data, OnCategoryClicked)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        ProductDetail(data, comments.size.toString())

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ProductComments(comments, OnAddNewComment)


    }


}

//--------------------------------------------------------------

@Composable
fun CommentBody(comment: Comment) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape = Shapes.large),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = Shapes.large
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = comment.userEmail,
                style = TextStyle(fontSize = 22.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = comment.text,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}


//----------------------------------------------------------------
@Composable
fun ProductComments(
    comments: List<Comment>,
    AddNewComment: (String) -> Unit
) {
    val ShowCommentDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (comments.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Comments",
                style = TextStyle
                    (
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(
                onClick = {
                    if (NetworkChecker(context).isInternetConnected) {
                        ShowCommentDialog.value = true
                    } else {
                        Toast.makeText(
                            context,
                            "Please Connected To Internet...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            {
                Text(
                    text = "Add New Comment",
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
        comments.forEach {
            CommentBody(it)
        }


    } else {
        TextButton(onClick = {
            if (NetworkChecker(context).isInternetConnected) {
                ShowCommentDialog.value = true
            } else {
                Toast.makeText(context, "connect to internet to add comment...", Toast.LENGTH_SHORT)
                    .show()
            }
        }) {

            Text(
                text = "Add New Comment",
                style = TextStyle(fontSize = 13.sp)
            )

        }

    }

    if (ShowCommentDialog.value) {
        AddNewCommentDialog(
            OnDismiss = { ShowCommentDialog.value = false },
            OnPositiveClick = {
                AddNewComment.invoke(it)
            }
        )
    }

}


//-------------------------------------------
@Composable
fun AddNewCommentDialog(
    OnDismiss: () -> Unit,
    OnPositiveClick: (String) -> Unit,
) {
    val context = LocalContext.current
    val userComment = remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = OnDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = Shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Write Your Comment...",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                //Enter data =>
                MainTextField(
                    edtValue = userComment.value,
                    hint = "Write Something..."
                ) {
                    userComment.value = it
                }


                //Buttons =>
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { OnDismiss.invoke() }) {
                        Text(text = "Cancel")
                    }

                    TextButton(onClick = {
                        if (userComment.value.isNotEmpty() && userComment.value.isNotBlank()) {
                            if (NetworkChecker(context).isInternetConnected) {
                                OnPositiveClick(userComment.value)
                                OnDismiss.invoke()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Connected To Internet First....",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(context, "Please Write First...", Toast.LENGTH_SHORT)
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


//------------------------------------------------------------------
@Composable
fun MainTextField(edtValue: String, hint: String, OnValueChanges: (String) -> Unit) {
    OutlinedTextField(
        label = { Text(text = hint) },
        value = edtValue,
        singleLine = false,
        maxLines = 2,
        onValueChange = OnValueChanges,
        placeholder = { Text(text = "Write Something...") },
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = Shapes.medium

    )

}


//--------------------------------------------------------------
@Composable
fun ProductDetail(data: Product, commentNumber: String) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_details_comment),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)

                )
                val commentText =
                    if (NetworkChecker(context).isInternetConnected) commentNumber + " Comments" else "No Internet"
                Text(
                    text = commentText,
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface

                )

            }
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_details_material),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = data.material,
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }

            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_details_sold),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = data.soldItem + " Sold",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }

        }
        Surface(
            modifier = Modifier
                .clip((Shapes.large))
                .align(Alignment.Bottom),
            color = Blue
        ) {
            Text(
                text = data.tags,
                color = Color.White,
                modifier = Modifier.padding(6.dp),
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
            )

        }
    }


}


//------------------------------------------------------------

@Composable
fun ProductDesign(data: Product, OnCategoryClicked: (String) -> Unit) {
    AsyncImage(
        model = data.imgUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(Shapes.medium)
    )
    Text(
        modifier = Modifier.padding(14.dp),
        text = data.name,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    )
    Text(
        text = data.detailText,
        color = MaterialTheme.colorScheme.onSurface,
        style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Justify)
    )
    TextButton(
        onClick = { OnCategoryClicked.invoke(data.category) }

    ) {
        Text(
            text = "#" + data.category,
            style = TextStyle(fontSize = 14.sp)
        )
    }

}

//------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductToolBar(
    productName: String,
    badgNumber: Int,
    onBackedClicked: () -> Unit,
    onCartClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onBackedClicked.invoke() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                text = productName,
                textAlign = TextAlign.Center
            )
        },
        actions = {
            Box {
                IconButton(onClick = { onCartClicked.invoke() }) {
                    Icon(Icons.Default.ShoppingCart, null)
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
        }
    )
}





//--------------------------------------------------------------------

@Composable
fun AddtoCart(
    price: String,
    isAddingProduct: Boolean,
    heightFraction: Float,
    OnCartClicked: () -> Unit)
{
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(heightFraction)

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .size(182.dp, 40.dp),
                onClick = { OnCartClicked.invoke() }
            ) {
                if (isAddingProduct) {
                    DotsTyping()
                } else {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Add Product To Cart...",
                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                        color = Color.White
                    )
                }

            }

            Surface(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(Shapes.large),
                color = Color.White
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 6.dp,
                            bottom = 6.dp),
                    text = stylePrice(price),
                    style = TextStyle(fontSize = 14.sp),
                    fontWeight = FontWeight.Medium,
                    color = Black
                )

            }

        }

    }

}

//-----------------------------------------------------------
@Composable
fun DotsTyping() {

    val dotSize = 10.dp
    val delayUnit = 350
    val maxOffset = 10f

    @Composable
    fun Dot(
        offset: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp

        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}

