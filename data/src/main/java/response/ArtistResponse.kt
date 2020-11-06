package response

import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    val id : Int,
    val user : UserResponse?,
    @SerializedName("next_shows") val nextShows : List<ShowResponse>?,
    @SerializedName("old_shows") val oldShows : List<ShowResponse>?,
    val bank : BankResponse?,
    @SerializedName("artistic_name") val artisticName : String,
    val verified : Boolean,
    @SerializedName("created_at") val createdAt : String,
    val lives : Int = 0,
    @SerializedName("test_next_shows") val testNextShows : List<ShowResponse>?,
    @SerializedName("test_old_shows") val testOldShows : List<ShowResponse>?
//    @SerializedName("followers_number") val followersNumber : Int
//    val biography : String,
//    @SerializedName("display_old_shows")val displayOldShows : Boolean,
//    @SerializedName("display_fans") val displayFans : Boolean,
//    @SerializedName("is_followed") val isFollowed: Boolean = false,
//    @SerializedName("following_number") val followingNumber : Int?,
//    @SerializedName("artist_message") val artistMessage : List<MessageResponse>?
)