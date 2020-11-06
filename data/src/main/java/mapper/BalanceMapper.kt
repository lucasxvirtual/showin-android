package mapper

import br.com.noclaftech.domain.model.Balance
import response.BalanceResponse
import javax.inject.Inject

class BalanceMapper @Inject constructor(){

    fun map(response : BalanceResponse?) : Balance? {
        if(response == null)
            return null
        return Balance(
            id = response.id,
            balanceOwner = response.balanceOwner,
            value = response.value,
            createdAt = response.createdAt
        )
    }
}