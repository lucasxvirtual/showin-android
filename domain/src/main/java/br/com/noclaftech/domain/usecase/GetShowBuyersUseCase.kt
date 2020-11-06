package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.FollowResult
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetShowBuyersUseCase @Inject constructor(private val showRepository: ShowRepository){

    fun execute(id: Int, page: Int): Observable<FollowResult> {
        return showRepository.getBuyers(id, page)
            .toObservable()
            .map { FollowResult.Success(it) as FollowResult }
            .onErrorReturn { FollowResult.Failure(it) }
            .startWith(FollowResult.Loading)
    }
}