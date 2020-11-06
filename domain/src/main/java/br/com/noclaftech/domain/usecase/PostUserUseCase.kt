package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.repository.AuthRepository
import helper.Helper
import io.reactivex.Observable
import javax.inject.Inject

class PostUserUseCase @Inject constructor(private val authRepository: AuthRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val user: User) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(username : String, email : String, name : String, birthday : String, password : String, gender : String): Observable<Result> {
        return authRepository.postUser(username, email, name, Helper.convertDateForApi(birthday), Helper.convertPassMd5(password), gender)
            .flatMap {
                authRepository.auth(email, Helper.convertPassMd5(password))
            }
            .flatMap {
                authRepository.saveToken(it.token)
                authRepository.getUser()
            }
            .toObservable()
            .map {
                authRepository.saveUser(it)
                Result.Success(it) as Result
            }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}