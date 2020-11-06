package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.PaginationExtract
import br.com.noclaftech.domain.repository.DonateRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetExtractUseCase @Inject constructor(private val donateRepository: DonateRepository){

    sealed class Result {
        object Loading : Result()
        data class Success(val paginationExtract: PaginationExtract) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(page : Int): Observable<Result> {
        return donateRepository.getExtract(page)
            .toObservable()
            .map { Result.Success(it) as Result }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
        
    }
}