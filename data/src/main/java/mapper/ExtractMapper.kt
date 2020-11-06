package mapper

import br.com.noclaftech.domain.model.Extract
import response.ExtractResponse
import javax.inject.Inject

class ExtractMapper @Inject constructor(private val balanceMapper: BalanceMapper){

    fun map(response : List<ExtractResponse>) : List<Extract>{
        return response.map { (map(it)) }
    }

    fun map(response : ExtractResponse) : Extract {
        return Extract(
            id = response.id,
            balanceFrom = balanceMapper.map(response.balanceFrom),
            balanceTo =  balanceMapper.map(response.balanceTo),
            reason = response.reason,
            isWithdrawal = response.isWithdrawal,
            value = response.value,
            createdAt = response.createdAt,
            price = response.price
        )
    }
}