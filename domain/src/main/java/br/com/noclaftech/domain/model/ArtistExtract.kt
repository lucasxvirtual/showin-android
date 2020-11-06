package br.com.noclaftech.domain.model

data class ArtistExtract(
    val coinPaidTickets: Int,
    val valuePaidTickets: Double,
    val coinDonations: Int,
    val valueDonations: Double,
    val platformCost: Float,
    val creditCardCost: Float?,
    val artistTotal: Float,
    val name: String,
    val date: String,
    val ecadCost: Float? = 0f,
    val percentage: Float? = 1f,
    val ecadPercent: Float? = null,
    val showinPercentage : Float? = null,
    val artistPercentage : Float? = null,
    val valuePaidTicketsReal : Float? = null,
    val valueDonationsReal : Float? = null,
    val artistTotalReal : Float? = null,
    val showinValue : Float? = null,
    val real : Boolean = false
)