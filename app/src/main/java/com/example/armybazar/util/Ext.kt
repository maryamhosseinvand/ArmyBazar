package com.example.armybazar.util

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler



val coroutineExceptionHandler = CoroutineExceptionHandler {
    _ ,throwable ->
    Log.e("error", "Error: ${throwable.message}", throwable)
}


//-----------------------------------------------


// برای جدا کردن سه رقم سه رقم ارقام

fun stylePrice(oldPrice: String): String {

    if (oldPrice.length > 3) {

        val reversed = oldPrice.reversed()
        var newPrice = ""

        for (i in oldPrice.indices) {
            newPrice += reversed[i]

            if ((i + 1) % 3 == 0) {
                newPrice += ','
            }

        }
        val readyToGo = newPrice.reversed()

        if(readyToGo.first() == ',') {
            return readyToGo.substring(1) + " Tomans"
        }

        return readyToGo + " Tomans"
    }

    return oldPrice + " Tomans"
}