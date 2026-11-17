package com.example.armybazar.ui.features.product

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.armybazar.model.Repository.Cart.CartRepository
import com.example.armybazar.model.Repository.Comment.CommentRepository
import com.example.armybazar.model.Repository.Product.ProductRepository
import com.example.armybazar.model.data.Comment
import com.example.armybazar.util.EMPTY_PRODUCT
import com.example.armybazar.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductScreenViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository : CartRepository
) : ViewModel() {

    val thisProduct = mutableStateOf(EMPTY_PRODUCT)
    val comments = mutableStateOf(listOf<Comment>())
    val  isAddingProduct = mutableStateOf(false)
    val badgeNumber =mutableStateOf(0)


    fun loadData(productId: String ,isInternetConnected : Boolean) {
        loadProductFromCache(productId)

        if (isInternetConnected){
          loadAllComments(productId)
            loadBadgeNumber()
        }

    }


    private fun loadProductFromCache(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value = productRepository.getProductById(productId)

        }

    }

    private fun loadAllComments(productId: String){

        viewModelScope.launch(coroutineExceptionHandler) {
             comments.value =commentRepository.getAllComments(productId)
        }
    }
     fun addNewComment(productId: String , text : String , IsSuccess : (String)-> Unit){
        viewModelScope.launch (coroutineExceptionHandler){
            commentRepository.addNewComment(productId,text,IsSuccess)
            delay(100)
            comments.value = commentRepository.getAllComments(productId) // Update comment list
        }
    }
    fun addProductToCart(productId: String , AddingToCartResult :(String)-> Unit){
        viewModelScope.launch(coroutineExceptionHandler) {

            isAddingProduct.value = true

            val result = cartRepository.addToCart(productId)
             delay(500)
            isAddingProduct.value= false

            if(result){
                AddingToCartResult.invoke("Product Added To Cart")
            } else{
                AddingToCartResult.invoke("Product  Not Added")
            }
        }

    }

    private fun loadBadgeNumber(){
        viewModelScope.launch (coroutineExceptionHandler){
            badgeNumber.value=cartRepository.getCartSize()
        }

    }


}