package com.example.armybazar.model.Repository.Cart

import com.example.armybazar.model.data.CheckOut
import com.example.armybazar.model.data.SubmitOrder
import com.example.armybazar.model.data.UserCartInfo

interface CartRepository {
    suspend fun addToCart(productId: String): Boolean
    suspend fun removeFromCart(productId: String): Boolean
    suspend fun getCartSize(): Int
    suspend fun getUserInfo(): UserCartInfo
    suspend fun submitOrder(address :String , postalCode: String) : SubmitOrder
    suspend fun checkOut(orderId: String): CheckOut

     fun setOrderId(orderId: String)
     fun getOrderId(): String

    fun setPurchaseStatus(status :Int)
    fun getPurchaseStatus():Int

}