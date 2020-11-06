package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.CoinsPrice
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class PostShowRevenueUseCase @Inject constructor(private val showRepository: ShowRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val coinsPrice: CoinsPrice) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(duration: Int, coin_amount: Int, category: Int): Observable<Result> {
        return showRepository.postShowRevenue(duration, coin_amount, category)
            .toObservable()
            .map { Result.Success(it) as Result }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)

    }
}