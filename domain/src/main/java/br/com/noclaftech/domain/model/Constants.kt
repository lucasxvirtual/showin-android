package br.com.noclaftech.domain.model

data class Constants(
    val ageRating : List<AgeRating>,
    val timezones : List<String>?,
    val config : Config,
    val packs : List<Pack>,
    val brands : List<Brand>,
    val categories : List<Category>,
    val genders : List<Gender>,
    val showDurations : List<ShowDuration>
)