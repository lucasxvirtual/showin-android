package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.LiveConfigResult
import br.com.noclaftech.domain.repository.StreamingRepository
import io.reactivex.Observable
import javax.inject.Inject

class ConfigUseCase @Inject constructor(private val streamingRepository: StreamingRepository) {


    fun execute(id: Int): Observable<LiveConfigResult> {
        return streamingRepository.getConfig(id)
            .toObservable()
            .map {
                LiveConfigResult.Success(it) as LiveConfigResult
            }
            .onErrorReturn { LiveConfigResult.Failure(it) }
            .startWith(LiveConfigResult.Loading)
    }

}