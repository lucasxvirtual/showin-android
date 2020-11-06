package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.WorkedResult
import br.com.noclaftech.domain.repository.WatchRepository
import io.reactivex.Observable
import javax.inject.Inject

class RemoveWatchUseCase @Inject constructor(private val watchRepository: WatchRepository) {

    fun execute(id : Int, deviceId : String): Observable<WorkedResult> {
        return watchRepository.removeWatch(id, deviceId)
            .toObservable()
            .map {  WorkedResult.Success(it) as WorkedResult }
            .onErrorReturn { WorkedResult.Failure(it) }
            .startWith(WorkedResult.Loading)
    }

}