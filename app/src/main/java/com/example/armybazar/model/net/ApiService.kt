package com.example.armybazar.model.net

import com.example.armybazar.model.Repository.TokenInMemory
import com.example.armybazar.model.data.AddNewCommentResponse
import com.example.armybazar.model.data.AdsResponse
import com.example.armybazar.model.data.CartResponse
import com.example.armybazar.model.data.CheckOut
import com.example.armybazar.model.data.CommentResponse
import com.example.armybazar.model.data.LoginResponse
import com.example.armybazar.model.data.ProductResponse
import com.example.armybazar.model.data.SubmitOrder
import com.example.armybazar.model.data.UserCartInfo
import com.example.armybazar.util.BASE_URL
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST(".")
    suspend fun signUp(
        @Query("method") method: String = "signUp",
        @Body jsonObject: JsonObject
    ): LoginResponse

    @POST(".")
    suspend fun signIn(
        @Query("method") method: String = "signIn",
        @Body jsonObject: JsonObject
    ): LoginResponse

    @GET(".")
    fun refreshToken(
        @Query("method") method: String = "refreshToken"
    ): Call<LoginResponse>

    @GET(".")
    suspend fun getAllProducts(
        @Query("method") method: String = "getProducts"
    ): ProductResponse

    @GET(".")
    suspend fun getAllAds(
        @Query("method") method: String = "getSliderPics"
    ): AdsResponse

    @POST(".")
    suspend fun getAllComments(
        @Query("method") method: String = "getComments" ,
        @Body jsonObject: JsonObject
    ): CommentResponse


    @POST(".")
    suspend fun addNewComment(
        @Query("method") method: String = "addNewComment" ,
        @Body jsonObject: JsonObject
    ): AddNewCommentResponse

    @POST(".")
    suspend fun addProductToCart(
        @Query("method") method: String = "addToCart" ,
        @Body jsonObject: JsonObject
    ): CartResponse

    @GET(".")
    suspend fun getUserCart(
        @Query("method") method: String = "getUserCart"
    ): UserCartInfo

    @POST(".")
    suspend fun removeProductToCart(
        @Query("method") method: String = "removeFromCart" ,
        @Body jsonObject: JsonObject
    ): CartResponse


    @POST(".")
    suspend fun submitOrder(
        @Query("method") method: String = "submitOrder" ,
        @Body jsonObject: JsonObject
    ): SubmitOrder



    @POST(".")
    suspend fun checkOut(
        @Query("method") method: String = "checkout" ,
        @Body jsonObject: JsonObject
    ): CheckOut


}

fun creatapiservice(): ApiService {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor() {
            val oldRequest = it.request()

            val newRequest = oldRequest.newBuilder()
            if (TokenInMemory.token != null)
                newRequest.addHeader("Authorization", TokenInMemory.token!!)
            newRequest.addHeader("Accept", "application/json")
            newRequest.method(oldRequest.method, oldRequest.body)

            return@addInterceptor it.proceed(newRequest.build())
        }
        .addInterceptor(logging)
            .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    return retrofit.create(ApiService::class.java)


}