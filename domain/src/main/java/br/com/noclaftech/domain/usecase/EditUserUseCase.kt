package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.repository.ArtistRepository
import br.com.noclaftech.domain.repository.AuthRepository
import helper.Helper
import io.reactivex.Observable
import javax.inject.Inject

class EditUserUseCase @Inject constructor(private val userRepository: AuthRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val user: User) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(id : Int, name : String, birthday : String, gender : String, biography : String, artisticName: String?): Observable<Result> {
        return userRepository.putUser(id, name, Helper.convertDateForApi(birthday), gender, biography, artisticName)
            .toObservable()
            .map {
                Result.Success(it) as Result
            }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}