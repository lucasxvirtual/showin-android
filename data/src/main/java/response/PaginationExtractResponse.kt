package response

data class PaginationExtractResponse(
    val count : Int,
    val next : String?,
    val previous : String?,
    val results : List<ExtractResponse>
)