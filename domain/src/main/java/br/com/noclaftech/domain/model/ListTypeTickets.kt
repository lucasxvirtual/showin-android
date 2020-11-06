package br.com.noclaftech.domain.model

data class ListTypeTickets(
    val old : List<Ticket>,
    val future : List<Ticket>
)