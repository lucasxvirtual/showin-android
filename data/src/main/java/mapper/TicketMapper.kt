package mapper

import br.com.noclaftech.domain.model.Ticket
import response.TicketResponse
import javax.inject.Inject

class TicketMapper @Inject constructor(){

    fun map(responseList: List<TicketResponse>) : List<Ticket>{
        return responseList.map{(map(it))}
    }

    fun map(response : TicketResponse) : Ticket {
        return Ticket(
            id = response.id,
            show = ShowMapper().map(response.show),
            value = response.value,
            value_paid = response.value_paid,
            user = response.user,
            count = response.count
        )
    }
}