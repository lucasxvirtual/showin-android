package response

data class FollowResponse(
    val id : Int,
    val username : String,
    val profile_image : String,
    val artist : Int?
)