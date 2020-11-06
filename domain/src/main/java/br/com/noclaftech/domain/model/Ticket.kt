package br.com.noclaftech.domain.model

data class Ticket(
    val id : Int,
    val show : Show,
    val value : Int,
    val value_paid : Int,
    val user : Int,
    var count : Int
)