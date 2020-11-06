package br.com.noclaftech.domain.model

import java.io.Serializable

data class Artist(
    val id : Int,
    val user : User?,
    val nextShows : List<Show>?,
    val oldShows : List<Show>?,
    val testNextShows : List<Show>?,
    val testOldShows : List<Show>?,
    val bank : Bank?,
    val artisticName : String,
    val verified : Boolean,
    val createdAt : String,
    val lives : Int = 0
//    var followersNumber : Int,
//    val biography : String?,
//    val displayOldShows : Boolean,
//    val displayFans : Boolean,
//    var isFollowed: Boolean = false,
//    val followingNumber : Int?,
//    val artistMessage : List<Message>?
) : Serializable