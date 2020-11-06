package response

import com.google.gson.annotations.SerializedName

data class CoinPriceResponse(
    @SerializedName("showin_percentage") val showinPercentage : Int,
    @SerializedName("percentage_artist") val percentageArtist : Int,
    @SerializedName("artist_value_per_ticket") val artistValuePerTicket: Float,
    @SerializedName("showin_value") val showinValue : Float
)