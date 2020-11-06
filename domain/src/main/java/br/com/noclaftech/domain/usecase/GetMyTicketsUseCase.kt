package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.ListTypeTickets
import br.com.noclaftech.domain.repository.TicketRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetMyTicketsUseCase @Inject constructor(private val ticketRepository: TicketRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val listTypeTickets: ListTypeTickets) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(): Observable<Result> {
        return ticketRepository.getMyTickets()
            .toObservable()
            .map { Result.Success(it) as Result }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}