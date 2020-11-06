package br.com.noclaftech.domain.model

import java.io.Serializable

data class PaginationNotification(
    val count : Int?,
    val next : String?,
    val previous : String?,
    val results : List<Notification>
): Serializable