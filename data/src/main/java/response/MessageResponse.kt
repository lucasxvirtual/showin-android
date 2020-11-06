package response

data class MessageResponse(
    val id : Int,
    val message : String,
    val created_at : String,
    val artist : Int
)