package br.com.noclaftech.domain.model

import java.io.Serializable

data class ShowDuration(
    val id : Int,
    val name : String,
    val minutes : Int
) : Serializable