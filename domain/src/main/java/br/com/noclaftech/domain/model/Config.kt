package br.com.noclaftech.domain.model

data class Config(
    val id : Int,
    val about : String,
    val talkToUsEmail : String,
    val privacyPolicy : String,
    val useTerms : String,
    val liveTutorialDoc : String,
    val minutesToConfig : Int,
    val minutesToShutDown : Int,
    val minutesToCancel : Int,
    val coinPrice : Float?,
    val howToCreateShow : String?,
    val streamTips : String?,
    val marketingTips : String?,
    val minimunPrice : Int?,
    val artistPercentage: Float? = 0.8f,
    val minimumPriceReal : Float? = 0f
)