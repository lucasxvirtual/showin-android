package br.com.noclaftech.domain.model

data class ShowAndArtist(
    val shows : List<ListTypeShow>,
    val artists : List<ListTypeArtist>?,
    val banners: List<Banner>?
)