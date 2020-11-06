package mapper

import br.com.noclaftech.domain.model.Payment
import response.PaymentResponse
import javax.inject.Inject

class PaymentMapper @Inject constructor(){
    fun map(responseList: List<PaymentResponse>) : List<Payment>{
        return responseList.map { (map(it)) }
    }

    fun map(response: PaymentResponse) : Payment {
        return Payment(
            id = response.id,
            status = response.status,
            created_at = response.created_at,
            pack = response.pack,
            transaction = response.transaction,
            user = response.user
        )
    }
}