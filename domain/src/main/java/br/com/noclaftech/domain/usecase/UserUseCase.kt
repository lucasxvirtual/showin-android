package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class UserUseCase @Inject constructor(private val userRepository: AuthRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val user: User) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(): Observable<Result> {
        return userRepository.getUser()
            .toObservable()
            .map {
                userRepository.saveUser(it)
                Result.Success(it) as Result
            }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}