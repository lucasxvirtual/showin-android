package br.com.noclaftech.domain.model

import java.io.Serializable

data class Extract(
    val id : Int,
    val balanceFrom : Balance?,
    val balanceTo : Balance?,
    val reason : String,
    val isWithdrawal : Boolean,
    val value : Int?,
    val createdAt : String?,
    val price : Float? = null
): Serializable