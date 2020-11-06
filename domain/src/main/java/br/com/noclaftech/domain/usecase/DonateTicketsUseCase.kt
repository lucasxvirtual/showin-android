package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.Worked
import br.com.noclaftech.domain.repository.TicketRepository
import io.reactivex.Observable
import javax.inject.Inject

class DonateTicketsUseCase @Inject constructor(private val ticketsRepository: TicketRepository) {
    sealed class Result {
        object Loading: Result()
        data class Success(val worked: Worked): Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(user: String, show: Int, count: Int): Observable<Result>{
        return ticketsRepository.postDonateTickets(user, show, count)
            .toObservable()
            .map { Result.Success(it) as Result}
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }

}