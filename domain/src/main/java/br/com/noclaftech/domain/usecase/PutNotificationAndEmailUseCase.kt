package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class PutNotificationAndEmailUseCase @Inject constructor(private val authRepository: AuthRepository){

    fun execute(id : Int, allow_notification : Boolean, allow_artist_email : Boolean, allow_commercial_email : Boolean): Observable<UserResult> {
        return authRepository.putUser(id, allow_notification, allow_artist_email, allow_commercial_email)
            .toObservable()
            .map {
                authRepository.saveUser(it)
                UserResult.Success(it) as UserResult
            }
            .onErrorReturn { UserResult.Failure(it) }
            .startWith(UserResult.Loading)
    }
}