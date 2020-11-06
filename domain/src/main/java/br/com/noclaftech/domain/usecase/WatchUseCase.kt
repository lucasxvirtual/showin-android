package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.Watch
import br.com.noclaftech.domain.repository.WatchRepository
import io.reactivex.Observable
import javax.inject.Inject

class WatchUseCase @Inject constructor(private val watchRepository: WatchRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val watch: Watch) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(id : Int, deviceId : String): Observable<Result> {
        return watchRepository.watch(id, deviceId)
            .toObservable()
            .map {  Result.Success(it) as Result }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}