package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.ListTypeTickets
import br.com.noclaftech.domain.model.Worked
import io.reactivex.Single

interface TicketRepository{
    fun getMyTickets() : Single<ListTypeTickets>

    fun postDonateTickets(user : String, show: Int, count: Int) : Single<Worked>

    fun deleteTicket(id: Int): Single<Worked>
}