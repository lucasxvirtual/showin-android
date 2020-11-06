package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class SocialNetworkUseCase @Inject constructor(private val authRepository: AuthRepository){

    fun execute(accessToken : String, socialNetwork : String, remember : Boolean): Observable<UserResult> {
        return authRepository.authSocialNetwork(accessToken, socialNetwork)
            .flatMap {
                authRepository.saveToken(it.token, remember)
                authRepository.getUser()
            }
            .toObservable()
            .map {
                authRepository.saveUser(it)
                UserResult.Success(it) as UserResult
            }
            .onErrorReturn { UserResult.Failure(it) }
            .startWith(UserResult.Loading)
    }
}