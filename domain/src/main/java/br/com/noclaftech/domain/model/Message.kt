package br.com.noclaftech.domain.model

import java.io.Serializable

data class Message(
    val id : Int,
    val message : String,
    val created_at : String,
    val artist : Int
) : Serializable