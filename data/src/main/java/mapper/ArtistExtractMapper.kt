package mapper

import br.com.noclaftech.domain.model.ArtistExtract
import response.ArtistExtractResponse
import javax.inject.Inject

class ArtistExtractMapper @Inject constructor() {

    fun map(responseList: List<ArtistExtractResponse>?) : List<ArtistExtract>?{
        if (responseList == null)
            return null
        return responseList.map{(map(it))}
    }

    fun map(response : ArtistExtractResponse) : ArtistExtract {
        return ArtistExtract(
            coinPaidTickets = response.coinPaidTickets,
            valuePaidTickets = response.valuePaidTickets,
            coinDonations = response.coinDonations,
            valueDonations = response.valueDonations,
            platformCost = response.platformCost,
            creditCardCost = response.creditCardCost,
            artistTotal = response.artistTotal,
            name = response.name,
            date = response.date,
            ecadCost = response.ecadCost,
            percentage = response.percentage,
            ecadPercent = response.ecadPercent,
            showinPercentage = response.showinPercentage,
            artistPercentage = response.artistPercentage,
            valuePaidTicketsReal = response.valuePaidTicketsReal,
            valueDonationsReal = response.valueDonationsReal,
            artistTotalReal = response.artistTotalReal,
            showinValue = response.showinValue,
            real = response.real
        )
    }
}