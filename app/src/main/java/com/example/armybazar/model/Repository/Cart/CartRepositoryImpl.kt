package com.example.armybazar.model.Repository.Cart

import android.content.SharedPreferences
import com.example.armybazar.model.data.CheckOut
import com.example.armybazar.model.data.SubmitOrder
import com.example.armybazar.model.data.UserCartInfo
import com.example.armybazar.model.net.ApiService
import com.example.armybazar.util.NO_PAYMENT
import com.google.gson.JsonObject

class CartRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref : SharedPreferences
): CartRepository {
    override suspend fun addToCart(productId: String): Boolean {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }
        val result = apiService.addProductToCart(jsonObject = jsonObject)
        return result.success
    }

    override suspend fun removeFromCart(productId: String): Boolean {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }
        val result = apiService.removeProductToCart(jsonObject = jsonObject)
        return result.success
    }


    override suspend fun getCartSize(): Int {

        val result = apiService.getUserCart()
        if (result.success) {
            var count = 0
            result.productList.forEach {
                count += (it.quantity ?: "0").toInt()
            }
            return count
        }
        return 0
    }

    override suspend fun getUserInfo(): UserCartInfo {
        return apiService.getUserCart()
    }

    override suspend fun submitOrder(
        address: String,
        postalCode: String
    ): SubmitOrder {
        val jsonObject = JsonObject().apply {
            addProperty("address",address)
            addProperty("postalCode",postalCode)
        }
        val result = apiService.submitOrder(jsonObject=jsonObject)
        setOrderId(result.orderId.toString())
        return result
    }

    override suspend fun checkOut(orderId: String): CheckOut {
      val jsonObject= JsonObject().apply {
          addProperty("orderId", orderId)
      }
        return apiService.checkOut(jsonObject=jsonObject)
    }

    override fun setOrderId(orderId: String) {
        sharedPref.edit().putString("orderId",orderId).apply()
    }

    override fun getOrderId(): String {
        return sharedPref.getString("orderId","0")!!
    }

    override fun setPurchaseStatus(status: Int) {
       sharedPref.edit().putInt("purchase_status",status).apply()  }

    override fun getPurchaseStatus(): Int {
       return sharedPref.getInt("purchase_status", NO_PAYMENT)
    }
}





