package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class EditNotificationTokenUseCase @Inject constructor(private val userRepository: AuthRepository){

    fun execute(id : Int, token: String): Observable<UserResult> {
        return userRepository.putUser(id, token)
            .toObservable()
            .map {
                UserResult.Success(it) as UserResult
            }
            .onErrorReturn { UserResult.Failure(it) }
            .startWith(UserResult.Loading)
    }
}