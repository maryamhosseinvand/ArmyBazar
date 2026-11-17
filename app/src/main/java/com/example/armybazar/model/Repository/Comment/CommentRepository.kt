package com.example.armybazar.model.Repository.Comment

import com.example.armybazar.model.data.Comment

interface CommentRepository {

    suspend fun getAllComments(productId: String): List<Comment>
    suspend fun addNewComment(productId: String , text : String , IsSuccess : (String)-> Unit)
}