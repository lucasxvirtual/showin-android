package api

import br.com.noclaftech.domain.model.ListTypeTickets
import io.reactivex.Single
import response.ListTypeTicketsResponse
import response.WorkedResponse
import javax.inject.Inject

class TicketApi @Inject constructor(private val ticketEndpoint: TicketEndpoint){
    fun getMyTickets() : Single<ListTypeTicketsResponse>{
        return ticketEndpoint.getMyTickets()
    }

    fun postDonateTicket(user : String, show: Int, count: Int): Single<WorkedResponse>{
        return ticketEndpoint.postDonateTicket(user, show, count)
    }

    fun deleteTicket(id : Int) : Single<WorkedResponse>{
        return ticketEndpoint.deleteTicket(id)
    }
}