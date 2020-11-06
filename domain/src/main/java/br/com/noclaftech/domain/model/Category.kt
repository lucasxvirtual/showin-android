package br.com.noclaftech.domain.model

import java.io.Serializable

data class Category(
    val id : Int,
    val name : String,
    val ecad: Float?
) : Serializable