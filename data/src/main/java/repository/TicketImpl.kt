package repository

import api.TicketApi
import br.com.noclaftech.domain.model.ListTypeTickets
import br.com.noclaftech.domain.model.Worked
import br.com.noclaftech.domain.repository.TicketRepository
import io.reactivex.Single
import mapper.ListTypeTicketsMapper
import mapper.WorkedMapper
import javax.inject.Inject

class TicketImpl @Inject constructor(
    private val ticketApi: TicketApi,
    private val workedMapper: WorkedMapper,
    private val listTypeTicketsMapper: ListTypeTicketsMapper
) : TicketRepository{

    override fun getMyTickets(): Single<ListTypeTickets> {
        return ticketApi.getMyTickets().map { listTypeTicketsMapper.map(it) }
    }

    override fun postDonateTickets(user: String, show: Int, count: Int): Single<Worked> {
        return ticketApi.postDonateTicket(user, show, count).map { workedMapper.map(it) }
    }

    override fun deleteTicket(id: Int): Single<Worked> {
        return ticketApi.deleteTicket(id).map { workedMapper.map(it) }
    }


}