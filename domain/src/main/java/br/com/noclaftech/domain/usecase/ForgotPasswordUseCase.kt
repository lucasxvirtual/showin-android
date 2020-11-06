package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.Worked
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(private val userRepository: AuthRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val worked: Worked) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }


    fun execute(email : String): Observable<Result> {
        return userRepository.forgotPassword(email)
            .toObservable()
            .map {
                Result.Success(it) as Result
            }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}