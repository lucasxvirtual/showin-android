package br.com.noclaftech.domain.model

import java.io.Serializable

data class Bank(
    val id : Int,
    val owner : String?,
    val bank : String?,
    val agency : String?,
    val account : String?,
    val cpf : String?,
    val cnpj : String?,
    val accountType : String?
) : Serializable