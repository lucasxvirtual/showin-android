package br.com.noclaftech.domain.model

data class Notification(
    val id : Int,
    val title : String,
    val message : String,
    val createdAt : String,
    val user : Int
)