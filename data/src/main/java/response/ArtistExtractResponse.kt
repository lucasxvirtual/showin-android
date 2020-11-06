package response

import com.google.gson.annotations.SerializedName

data class ArtistExtractResponse (
    @SerializedName("coin_paid_tickets") val coinPaidTickets: Int,
    @SerializedName("value_paid_tickets") val valuePaidTickets: Double,
    @SerializedName("coin_donations") val coinDonations: Int,
    @SerializedName("value_donations") val valueDonations: Double,
    @SerializedName("platform_cost") val platformCost: Float,
    @SerializedName("credit_card_cost") val creditCardCost: Float?,
    @SerializedName("artist_total") val artistTotal: Float,
    val name: String,
    val date: String,
    @SerializedName("ecad_cost") val ecadCost: Float?,
    val percentage: Float? = 1f,
    @SerializedName("ecad_percent") val ecadPercent: Float? = null,
    @SerializedName("showin_percentage") val showinPercentage : Float? = null,
    @SerializedName("artist_percentage") val artistPercentage : Float? = null,
    @SerializedName("value_paid_tickets_real") val valuePaidTicketsReal : Float? = null,
    @SerializedName("value_donations_real") val valueDonationsReal : Float? = null,
    @SerializedName("artist_total_real") val artistTotalReal : Float? = null,
    @SerializedName("showin_value") val showinValue : Float? = null,
    val real : Boolean = false
)