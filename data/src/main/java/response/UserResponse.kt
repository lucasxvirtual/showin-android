package response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id : Int,
    @SerializedName("profile_image") val profileImage : String?,
    @SerializedName("is_artist") val isArtist : Boolean,
    @SerializedName("is_active") val isActive : Boolean,
    val balance : BalanceResponse?,
    val following : Int,
    val followers : Int = 0,
    val username : String,
    val email : String,
    val artist : ArtistResponse?,
    @SerializedName("user_messages") val userMessages : List<MessageResponse>?,
    @SerializedName("is_followed") val isFollowed : Boolean,
    @SerializedName("last_login") val lastLogin : String?,
    val name : String,
    val gender: String?,
    @SerializedName("is_admin") val isAdmin : Boolean,
    @SerializedName("register_date") val registerDate : String,
    @SerializedName("verified_email") val verifiedEmail : Boolean,
    val birthday : String,
    @SerializedName("allow_notification") val allowNotification : Boolean,
    @SerializedName("allow_artist_email") val allowArtistEmail : Boolean,
    @SerializedName("allow_commercial_email") val allowCommercialEmail : Boolean,
    @SerializedName("token_notification") val tokenNotification : String?,
    val linkedin : String?,
    val facebook: String?,
    val whatsapp: String?,
    val twitter: String?,
    val instagram: String?,
    val bio: String?,
    @SerializedName("time_zone") val timeZone : String
)