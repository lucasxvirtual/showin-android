package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.WorkedResult
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.repository.ArtistRepository
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class FollowUseCase @Inject constructor(private val artistRepository: ArtistRepository) {

    fun execute(user: User): Observable<WorkedResult>{
        return artistRepository.follow(user.id)
            .toObservable()
            .map {
                WorkedResult.Success(it) as WorkedResult
            }
            .onErrorReturn { WorkedResult.Failure(it) }
            .startWith(WorkedResult.Loading)
    }
}