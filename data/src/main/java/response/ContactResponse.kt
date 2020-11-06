package response

data class ContactResponse(
    val id : Int,
    val title : String,
    val message : String,
    val created_at : String,
    val user : Int
)