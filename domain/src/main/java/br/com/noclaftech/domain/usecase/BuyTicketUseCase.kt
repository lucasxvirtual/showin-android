package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.Ticket
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class BuyTicketUseCase @Inject constructor(private val showRepository: ShowRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val ticket: Ticket) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(id : Int, value: Float, countTickets: Int): Observable<Result> {
        return showRepository.buyTicket(id, value,countTickets)
            .toObservable()
            .map {Result.Success(it) as Result}
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}