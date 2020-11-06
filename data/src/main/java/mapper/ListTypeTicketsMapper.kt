package mapper

import br.com.noclaftech.domain.model.ListTypeTickets
import response.ListTypeTicketsResponse
import javax.inject.Inject

class ListTypeTicketsMapper @Inject constructor(){
    fun map(responseList: List<ListTypeTicketsResponse>) : List<ListTypeTickets>{
        return responseList.map { (map(it)) }
    }

    fun map(response: ListTypeTicketsResponse) : ListTypeTickets{
        return ListTypeTickets(
            old = TicketMapper().map(response.old),
            future = TicketMapper().map(response.future)
        )
    }
}