package com.example.armybazar.model.Repository.Product

import com.example.armybazar.model.data.Ads
import com.example.armybazar.model.data.Product

interface ProductRepository {

    suspend fun getAllProducts(isInternetConnected: Boolean): List<Product>
    suspend fun getAllAds(isInternetConnected: Boolean): List<Ads>

    suspend fun getAllProductsByCategory( category: String) : List<Product>
    suspend fun getProductById(productId: String) : Product
}