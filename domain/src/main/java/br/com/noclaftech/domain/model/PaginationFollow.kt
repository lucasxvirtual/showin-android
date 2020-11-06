package br.com.noclaftech.domain.model

import java.io.Serializable

data class PaginationFollow(
    val count : Int?,
    val next : String?,
    val previous : String?,
    val results : List<Follow>
): Serializable