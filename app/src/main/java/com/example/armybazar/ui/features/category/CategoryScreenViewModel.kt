package com.example.armybazar.ui.features.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.armybazar.model.Repository.Product.ProductRepository
import com.example.armybazar.model.data.Product
import kotlinx.coroutines.launch

class CategoryScreenViewModel(
    private val productRepository: ProductRepository,
) : ViewModel() {

    val dataProducts = mutableStateOf<List<Product>>(listOf())


     fun loadDataByCategory( category : String) {
        viewModelScope.launch {

            val dataFromLocal = productRepository.getAllProductsByCategory(category)
            dataProducts.value = dataFromLocal

        }


    }


}