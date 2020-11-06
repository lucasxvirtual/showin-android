package response

data class PackResponse(
    val id : Int,
    val coin_amout : Int,
    val price : Float,
    val created_at : String
)