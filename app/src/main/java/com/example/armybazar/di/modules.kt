package com.example.armybazar.di

import android.content.Context
import androidx.room.Room
import com.example.armybazar.model.Repository.Cart.CartRepository
import com.example.armybazar.model.Repository.Cart.CartRepositoryImpl
import com.example.armybazar.model.Repository.Comment.CommentRepository
import com.example.armybazar.model.Repository.Comment.CommentRepositoryImpl
import com.example.armybazar.model.Repository.Product.ProductRepository
import com.example.armybazar.model.Repository.Product.ProductRepositoryImpl
import com.example.armybazar.model.Repository.User.UserRepository
import com.example.armybazar.model.Repository.User.UserRepositoryImpl
import com.example.armybazar.model.db.AppDatabase
import com.example.armybazar.model.net.creatapiservice
import com.example.armybazar.ui.features.cart.CartViewModel
import com.example.armybazar.ui.features.category.CategoryScreenViewModel
import com.example.armybazar.ui.features.main.MainScreenViewModel
import com.example.armybazar.ui.features.product.ProductScreenViewModel
import com.example.armybazar.ui.features.profile.ProfileViewModel
import com.example.armybazar.ui.features.signIn.SignInViewModel
import com.example.armybazar.ui.features.signup.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {

    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { creatapiservice() }

    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_dataBase.db")
        //.addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration(true)
        .build() }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get<AppDatabase>().productDao()) }
    single<CommentRepository> { CommentRepositoryImpl(get())}
    single<CartRepository> { CartRepositoryImpl(get(), get())}

    viewModel { CartViewModel(get() , get() )}
    viewModel { ProfileViewModel(get() )}
    viewModel { ProductScreenViewModel(get() , get(), get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { (isNetConnected: Boolean) -> MainScreenViewModel(get(),get() ,isNetConnected) }
    viewModel { CategoryScreenViewModel(get()) }

}