package response

data class ListTypeArtistResponse(
    val title : String,
    val type : String,
    val data: List<ArtistResponse>
)