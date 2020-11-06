package response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ShowResponse(
    val id : Int,
    @SerializedName("age_rating_obj") val ageRatingResponse: AgeRatingResponse,
    val is_purchased : Boolean,
    val category_obj : CategoryResponse,
    @SerializedName("vertical_image") val verticalImage: String?,
    @SerializedName("horizontal_image") val horizontalImage: String?,
    @SerializedName("artist_artistic_name") val artistArtisticName: String,
    @SerializedName("age_rating") val ageRating: String,
    val name : String,
    val date: String,
    @SerializedName("date_finish") val dateFinish: String,
    val description: String,
    @SerializedName("send_viewer_email") val sendViewerEmail: Boolean,
    @SerializedName("ticket_limit") val ticketLimit: Int?,
    @SerializedName("send_artist_email") val sendArtistEmail: Boolean,
    val ticket_sold : Int,
    @SerializedName("ticket_value") val ticketValue: Int,
    val created_at: String,
    val artist: Int,
    val category: Int,
    val pay_what_you_can : Boolean,
    val status : String,
    val config_time : String?,
    val live_time : String?,
    val done_time : String?,
    val canceled_time : String?,
    val set_list : String?,
    val position: String?,
    val display_viewers : Boolean = false,
    val live_test : Boolean,
    @SerializedName("ticket_price") val ticketPrice : Float? = null
) : Serializable