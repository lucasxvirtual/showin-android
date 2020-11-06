package br.com.noclaftech.domain.model

import java.io.Serializable

data class Report(
    val id : Int,
    val chatMessage : String,
    val reason : String,
    val createdAt : String,
    val userReported : Int,
    val userReporting : Int
) : Serializable