package response

import com.google.gson.annotations.SerializedName
import retrofit2.http.FormUrlEncoded
import java.io.Serializable
import java.util.*

data class ShowDetailsResponse(
    val id : Int,
    val artist : ArtistResponse,
    val name : String,
    val date : String,
    @SerializedName("date_finish") val dateFinish : String,
    val description : String,
    @SerializedName("age_rating") val ageRating : String,
    @SerializedName("send_artist_email") val sendArtistEmail : Boolean = false,
    @SerializedName("send_viewer_email") val sendViewerEmail : Boolean = false,
    @SerializedName("ticket_limit") val ticketLimit : Int?,
    @SerializedName("ticket_value") val ticketValue : Int,
    @SerializedName("vertical_image") val verticalImage : String?,
    @SerializedName("horizontal_image") val horizontalImage : String?,
    val created_at : String,
    val category: Int,
    val category_obj : CategoryResponse,
    @SerializedName("age_rating_obj") val ageRatingObj : AgeRatingResponse,
    val is_purchased : Boolean = false,
    val ticket_sold : Int,
    val pay_what_you_can : Boolean = false,
    val status : String,
    val config_time : String?,
    val live_time : String?,
    val done_time : String?,
    val canceled_time : String?,
    val set_list : String?,
    val position : String?,
    var display_viewers : Boolean = true,
    var live_test : Boolean,
    @SerializedName("ticket_price") val ticketPrice : Float? = null
) : Serializable