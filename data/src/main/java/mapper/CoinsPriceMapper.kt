package mapper

import br.com.noclaftech.domain.model.CoinsPrice
import response.CoinsPriceObjResponse
import javax.inject.Inject

class CoinsPriceMapper @Inject constructor(private val coinPriceMapper: CoinPriceMapper) {

    fun map(response: CoinsPriceObjResponse): CoinsPrice {
        return CoinsPrice(
            coin_price = coinPriceMapper.map(response.coin_price)
        )
    }
}