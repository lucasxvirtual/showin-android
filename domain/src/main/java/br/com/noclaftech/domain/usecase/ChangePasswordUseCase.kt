package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.repository.AuthRepository
import helper.Helper
import io.reactivex.Observable
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val userRepository: AuthRepository) {
    sealed class Result {
        object Loading : Result()
        data class Success(val user: User) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(id: Int, passwordOld: String, passwordNew: String): Observable<Result> {
        return userRepository.putPassword(id, Helper.convertPassMd5(passwordOld), Helper.convertPassMd5(passwordNew))
            .toObservable()
            .map {
                userRepository.saveUser(it)
                Result.Success(it) as Result
            }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }

}


