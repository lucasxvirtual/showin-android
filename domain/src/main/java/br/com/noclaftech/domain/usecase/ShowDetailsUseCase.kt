package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.ShowDetails
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class ShowDetailsUseCase @Inject constructor(private val showDetailsRepository: ShowRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val showDetails: ShowDetails) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(id: Int): Observable<Result> {

        return showDetailsRepository.getShow(id)
            .toObservable()
            .map { Result.Success(it) as Result }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}