package br.com.noclaftech.domain.model

import java.io.Serializable

data class User(
    val id : Int,
    val profileImage : String?,
    val isArtist : Boolean,
    val isActive : Boolean,
    val balance : Balance?,
    var following : Int,
    var followers : Int,
    val username : String,
    val email : String,
    val artist : Artist?,
    val userMessages : List<Message>?,
    var isFollowed : Boolean,
    val lastLogin : String?,
    val name : String,
    val gender: String?,
    val isAdmin : Boolean,
    val registerDate : String,
    val verifiedEmail : Boolean,
    val birthday : String?,
    val allowNotification : Boolean,
    val allowArtistEmail : Boolean,
    val allowCommercialEmail : Boolean,
    val tokenNotification : String?,
    val linkedin : String?,
    val facebook: String?,
    val whatsapp: String?,
    val twitter: String?,
    val instagram: String?,
    val bio: String?,
    val timeZone : String
) : Serializable