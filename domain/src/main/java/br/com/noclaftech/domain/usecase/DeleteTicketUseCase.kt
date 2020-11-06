package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.Worked
import br.com.noclaftech.domain.repository.TicketRepository
import io.reactivex.Observable
import javax.inject.Inject

class DeleteTicketUseCase @Inject constructor(private val ticketRepository: TicketRepository) {

    sealed class Result {
        object Loading: Result()
        data class Success(val worked: Worked): Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(id: Int) : Observable<Result>{
        return ticketRepository.deleteTicket(id)
            .toObservable()
            .map { Result.Success(it) as Result }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}