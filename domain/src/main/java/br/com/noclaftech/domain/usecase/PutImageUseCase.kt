package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class PutImageUseCase @Inject constructor(private val authRepository: AuthRepository){

    fun execute(id : Int, image : String): Observable<UserResult> {
        return authRepository.putImage(id, image)
            .toObservable()
            .map {
                UserResult.Success(it) as UserResult
            }
            .onErrorReturn { UserResult.Failure(it) }
            .startWith(UserResult.Loading)
    }
}