package com.example.armybazar.model.Repository.Product

import com.example.armybazar.model.data.Ads
import com.example.armybazar.model.data.Product
import com.example.armybazar.model.db.ProductDao
import com.example.armybazar.model.net.ApiService

class ProductRepositoryImpl(
    private val apiService: ApiService,
    private val productDao:ProductDao

): ProductRepository{
    override suspend fun getAllProducts(isInternetConnected: Boolean): List<Product> {
        if (isInternetConnected){

            // get from net
            val dataFromServer =apiService.getAllProducts()
            if (dataFromServer.success){
                productDao.InsertOrUpdate(dataFromServer.products)
                return dataFromServer.products
            }

        } else{
            // get from dao
            return productDao.getAll()

        }

      return listOf()
    }

    override suspend fun getAllAds(isInternetConnected: Boolean): List<Ads> {
        if (isInternetConnected){
            // get all ads
            val dataFromServer=apiService.getAllAds()
            if (dataFromServer.success){
              return dataFromServer.ads

            }


        }
        return listOf()
    }

    override suspend fun getAllProductsByCategory(category: String): List<Product> {
        return productDao.getAllByCategory(category)
    }

    override suspend fun getProductById(productId: String) : Product {
        return productDao.getProductById(productId)

    }

}
