package br.com.noclaftech.domain.model

import java.io.Serializable

data class Pack(
    val id : Int,
    val coin_amout : Int,
    val price : Float,
    val created_at : String
) : Serializable