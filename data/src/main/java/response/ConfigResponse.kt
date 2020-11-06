package response

import com.google.gson.annotations.SerializedName

data class ConfigResponse(
    val id : Int,
    val about : String,
    @SerializedName("talk_to_us_email") val talkToUsEmail : String,
    @SerializedName("privacy_policy") val privacyPolicy : String,
    @SerializedName("use_terms") val useTerms : String,
    @SerializedName("live_tutorial_doc") val liveTutorialDoc : String,
    @SerializedName("minutes_to_config") val minutesToConfig : Int,
    @SerializedName("minutes_to_shut_down") val minutesToShutDown : Int,
    @SerializedName("minutes_to_cancel") val minutesToCancel : Int,
    @SerializedName("coin_price") val coinPrice : Float?,
    val how_to_create_show : String?,
    val stream_tips : String?,
    val marketing_tips : String?,
    val minimun_price : Int?,
    @SerializedName("artist_percentage") val artistPercentage : Float? = 0.8f,
    @SerializedName("minimun_price_real") val minimumPriceReal : Float? = 0f
)