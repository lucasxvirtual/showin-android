package br.com.noclaftech.domain.model

data class Payment(
    val id : Int,
    val status : String,
    val created_at : String,
    val pack : Int,
    val transaction : Int,
    val user : Int
)