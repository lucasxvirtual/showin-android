package response

data class PaginationNotificationResponse(
    val count : Int,
    val next : String?,
    val previous : String?,
    val results : List<NotificationResponse>
)