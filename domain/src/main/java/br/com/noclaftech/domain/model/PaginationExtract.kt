package br.com.noclaftech.domain.model

import java.io.Serializable


data class PaginationExtract(
    val count : Int?,
    val next : String?,
    val previous : String?,
    val results : List<Extract>
): Serializable