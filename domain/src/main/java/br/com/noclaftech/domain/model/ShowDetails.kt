package br.com.noclaftech.domain.model

import java.io.Serializable

class ShowDetails(
    val id: Int,
    val artist: Artist,
    val name : String,
    val date : String,
    val dateFinish : String,
    val description : String,
    val ageRating : String,
    val sendArtistEmail : Boolean = false,
    val sendViewerEmail: Boolean = false,
    val ticketLimit : Int?,
    val ticketValue : Int,
    val verticalImage : String?,
    val horizontalImage : String?,
    val createdAt : String,
    val category: Int,
    val categoryObj : Category,
    val ageRatingObg : AgeRating,
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
    var displayViewers : Boolean = true,
    val liveTest: Boolean,
    val ticketPrice : Float? = null
) : Serializable {

    fun toShow(): Show {
        return Show(id, verticalImage, horizontalImage, "", name, date, dateFinish, description, ageRating, sendArtistEmail, sendViewerEmail,
            ticketLimit, ticketValue, createdAt, category, categoryObj, ageRatingObg, isPurchased, ticketSold, payWhatYouCan, status, configTime, liveTime, doneTime, canceledTime, setList, position, displayViewers, liveTest)
    }

    fun getPriceFormatted() = String.format("%.2f", ticketPrice).replace(".", ",")

}