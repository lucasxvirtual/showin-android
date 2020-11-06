package response

data class ListTypeShowResponse(
    val title : String,
    val type : String,
    val data: List<ShowResponse>
)