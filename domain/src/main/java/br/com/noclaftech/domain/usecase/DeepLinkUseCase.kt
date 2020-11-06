package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.DeepLinkResult
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class DeepLinkUseCase @Inject constructor(private val showRepository: ShowRepository){

    fun execute(id: Int): Observable<DeepLinkResult> {
        return showRepository.createDeepLink(id)
            .toObservable()
            .map {
                DeepLinkResult.Success(it) as DeepLinkResult
            }
            .onErrorReturn { DeepLinkResult.Failure(it) }
            .startWith(DeepLinkResult.Loading)
    }
}