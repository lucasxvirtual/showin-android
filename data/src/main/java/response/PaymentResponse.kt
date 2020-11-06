package response

data class PaymentResponse(
    val id : Int,
    val status : String,
    val created_at : String,
    val pack : Int,
    val transaction : Int,
    val user : Int
)