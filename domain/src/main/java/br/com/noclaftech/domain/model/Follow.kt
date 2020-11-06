package br.com.noclaftech.domain.model

data class Follow(
    val id : Int,
    val username : String,
    val profileImage : String,
    val artist : Int?
)