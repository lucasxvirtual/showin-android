package br.com.noclaftech.domain.model

import java.io.Serializable

data class Balance(
    val id : Int,
    val balanceOwner : String?,
    val value : Int?,
    val createdAt : String?
) : Serializable