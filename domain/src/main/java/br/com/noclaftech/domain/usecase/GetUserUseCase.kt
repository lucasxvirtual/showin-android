package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.repository.ArtistRepository
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val authRepository: AuthRepository){

    fun execute(id : Int): Observable<UserResult> {
        return authRepository.getUser(id)
            .toObservable()
            .map {
                UserResult.Success(it) as UserResult
            }
            .onErrorReturn { UserResult.Failure(it) }
            .startWith(UserResult.Loading)
    }
}