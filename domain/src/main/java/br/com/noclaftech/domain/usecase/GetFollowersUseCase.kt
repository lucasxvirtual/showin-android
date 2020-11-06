package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.FollowResult
import br.com.noclaftech.domain.repository.FollowRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetFollowersUseCase @Inject constructor(private val followRepository: FollowRepository){

    fun execute(id: Int, page : Int): Observable<FollowResult> {
        return followRepository.getFollowers(id, page)
            .toObservable()
            .map { FollowResult.Success(it) as FollowResult }
            .onErrorReturn { FollowResult.Failure(it) }
            .startWith(FollowResult.Loading)
    }
}