package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.repository.ArtistRepository
import br.com.noclaftech.domain.repository.AuthRepository
import helper.Helper
import io.reactivex.Observable
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository,
                                      private val artistRepository: ArtistRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val worked: User) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(username : String, password : String, remember : Boolean): Observable<UserUseCase.Result> {
        return authRepository.auth(username, Helper.convertPassMd5(password))
            .flatMap {
                authRepository.saveToken(it.token, remember)
                authRepository.getUser()
            }
            .toObservable()
            .map {
                authRepository.saveUser(it)
                UserUseCase.Result.Success(it) as UserUseCase.Result
            }
            .onErrorReturn { UserUseCase.Result.Failure(it) }
            .startWith(UserUseCase.Result.Loading)
    }
}