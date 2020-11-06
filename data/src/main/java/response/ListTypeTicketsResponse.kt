package response

data class ListTypeTicketsResponse(
    val old : List<TicketResponse>,
    val future : List<TicketResponse>
)