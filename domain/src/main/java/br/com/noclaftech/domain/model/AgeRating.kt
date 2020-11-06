package br.com.noclaftech.domain.model

import java.io.Serializable

data class AgeRating(
    val id : Int,
    val age : String,
    val image : String
) : Serializable