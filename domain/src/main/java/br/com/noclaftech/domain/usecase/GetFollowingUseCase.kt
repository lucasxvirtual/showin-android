package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.FollowResult
import br.com.noclaftech.domain.repository.FollowRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetFollowingUseCase @Inject constructor(private val followRepository: FollowRepository){

    fun execute(id: Int, page : Int): Observable<FollowResult> {
        return followRepository.getFollowing(id, page)
            .toObservable()
            .map { FollowResult.Success(it) as FollowResult }
            .onErrorReturn { FollowResult.Failure(it) }
            .startWith(FollowResult.Loading)
    }
}