package br.com.noclaftech.domain.model

data class Contact(
    val id : Int,
    val title : String,
    val message : String,
    val created_at : String,
    val user : Int
)