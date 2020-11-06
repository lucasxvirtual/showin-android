package br.com.noclaftech.domain.model


data class ChatMessage(
    var id: Int?,
    var username: String?,
    var verified: Boolean,
    var photo: String?,
    var message: String?,
    var messageType: String?
)