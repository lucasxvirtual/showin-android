package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.Constants
import br.com.noclaftech.domain.repository.ConstantsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetConstantsUseCase @Inject constructor(private val constantsRepository: ConstantsRepository){

    sealed class Result {
        object Loading : Result()
        data class Success(val constants: Constants) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(): Observable<Result> {
        return constantsRepository.getConstants()
            .toObservable()
            .map {
                constantsRepository.saveConstants(it)
                Result.Success(it) as Result
            }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}