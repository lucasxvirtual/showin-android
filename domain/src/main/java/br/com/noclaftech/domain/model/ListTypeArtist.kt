package br.com.noclaftech.domain.model

data class ListTypeArtist(
    val title : String,
    val type : String,
    val data : List<Artist>
)