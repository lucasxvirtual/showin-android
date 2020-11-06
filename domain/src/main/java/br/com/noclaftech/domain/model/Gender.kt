package br.com.noclaftech.domain.model

import java.io.Serializable

data class Gender(
    val id : Int,
    val name : String,
    val value : String
) : Serializable