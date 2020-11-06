package br.com.noclaftech.domain.model

import java.io.Serializable

class Show(
    val id : Int,
    val verticalImage: String?,
    val horizontalImage: String?,
    val artistArtisticName: String,
    val name : String,
    val date: String,
    val dateFinish: String,
    val description: String,
    val ageRating: String,
    val sendArtistEmail: Boolean = false,
    val sendViewerEmail: Boolean = false,
    val ticketLimit: Int?,
    val ticketValue: Int,
    val createdAt: String,
//    val artist: Int,
    val category: Int,
    val categoryObj : Category,
    val ageRatingObj: AgeRating,
    val isPurchased : Boolean = false,
    val ticketSold : Int,
    val payWhatYouCan : Boolean = false,
    val status : String,
    val configTime : String?,
    val liveTime : String?,
    val doneTime : String?,
    val canceledTime : String?,
    val setList : String?,
    val position: String?,
    val displayViewers : Boolean = false,
    val liveTest : Boolean,
    val ticketPrice : Float? = null
) : Serializable {

    fun getPriceFormatted() = String.format("%.2f", ticketPrice).replace(".", ",")

}