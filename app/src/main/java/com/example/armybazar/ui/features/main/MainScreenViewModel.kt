package com.example.armybazar.ui.features.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.armybazar.model.Repository.Cart.CartRepository
import com.example.armybazar.model.Repository.Product.ProductRepository
import com.example.armybazar.model.data.Ads
import com.example.armybazar.model.data.CheckOut
import com.example.armybazar.model.data.Product
import com.example.armybazar.util.coroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository : CartRepository,
    isInternetConnected: Boolean
) : ViewModel() {

    val dataProducts = mutableStateOf<List<Product>>(listOf())
    val dataAds = mutableStateOf<List<Ads>>(listOf())
    val showProgresBar = mutableStateOf(false)
    val badgeNumber =mutableStateOf(0)

    val showPaymentResultDialog = mutableStateOf(false)
    val checkoutData = mutableStateOf(CheckOut(null, null))

    init {
        refreshAllDataFromNet(isInternetConnected)
    }

    fun getCheckoutData() {

        viewModelScope.launch(coroutineExceptionHandler) {

            val result = cartRepository.checkOut(cartRepository.getOrderId())
            if (result.success!!) {
                checkoutData.value = result
                showPaymentResultDialog.value = true
            }

        }

    }

    fun getPaymentStatus(): Int {
        return cartRepository.getPurchaseStatus()
    }

    fun setPaymentStatus(status: Int) {
        cartRepository.setPurchaseStatus(status)
    }

    private fun refreshAllDataFromNet(isInternetConnected: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {
                showProgresBar.value = true
            android.util.Log.d("PROGRESS_TEST", "showProgresBar = ${showProgresBar.value}")

            delay(4000)

            val newDataProducts = async { productRepository.getAllProducts(isInternetConnected) }
            val newaDataAds = async { productRepository.getAllAds(isInternetConnected) }

            updateData(newDataProducts.await(), newaDataAds.await())

            showProgresBar.value =false
            android.util.Log.d("PROGRESS_TEST", "showProgresBar = ${showProgresBar.value}")


        }


    }

    private fun CoroutineScope.updateData(product: List<Product>, ads: List<Ads>) {
        dataProducts.value = product
        dataAds.value = ads

    }

     fun loadBadgeNumber(){
        viewModelScope.launch (coroutineExceptionHandler){
            badgeNumber.value=cartRepository.getCartSize()
        }

    }

}