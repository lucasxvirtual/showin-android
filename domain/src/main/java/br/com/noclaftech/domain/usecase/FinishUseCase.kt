package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.WorkedResult
import br.com.noclaftech.domain.repository.StreamingRepository
import io.reactivex.Observable
import javax.inject.Inject

class FinishUseCase @Inject constructor(private val streamingRepository: StreamingRepository) {


    fun execute(id: Int): Observable<WorkedResult> {
        return streamingRepository.finish(id)
            .toObservable()
            .map {
                WorkedResult.Success(it) as WorkedResult
            }
            .onErrorReturn { WorkedResult.Failure(it) }
            .startWith(WorkedResult.Loading)
    }

}