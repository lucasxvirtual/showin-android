package response

data class TicketResponse(
    val id : Int,
    val show : ShowResponse,
    val value : Int,
    val value_paid : Int,
    val user : Int,
    val count : Int
)