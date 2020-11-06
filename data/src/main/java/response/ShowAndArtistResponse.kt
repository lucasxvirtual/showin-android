package response

data class ShowAndArtistResponse(
    val shows : List<ListTypeShowResponse>,
    val artists : List<ListTypeArtistResponse>?,
    val banners : List<BannerResponse>
)