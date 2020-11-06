package response

data class NotificationResponse(
    val id : Int,
    val title : String,
    val message : String,
    val created_at : String,
    val user : Int
)