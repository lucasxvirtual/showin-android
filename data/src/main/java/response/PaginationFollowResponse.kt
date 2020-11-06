package response

data class PaginationFollowResponse(
    val count : Int,
    val next : String?,
    val previous : String?,
    val results : List<FollowResponse>
)