package mapper

import br.com.noclaftech.domain.model.CoinPrice
import br.com.noclaftech.domain.model.CoinsPrice
import response.CoinPriceResponse
import response.CoinsPriceObjResponse
import javax.inject.Inject

class CoinPriceMapper @Inject constructor() {

    fun map(response: CoinPriceResponse): CoinPrice {
        return CoinPrice(
            showinPercentage = response.showinPercentage,
            percentageArtist = response.percentageArtist,
            artistValuePerTicket = response.artistValuePerTicket,
            showinValue = response.showinValue
        )
    }
}