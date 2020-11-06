package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class PutSocialsUseCase @Inject constructor(private val authRepository: AuthRepository){
    fun execute(id : Int, whatsapp : String?, instagram : String?, facebook : String?, linkedin : String?, twitter : String?): Observable<UserResult> {
        return authRepository.putSocials(id, whatsapp, instagram, facebook, linkedin, twitter)
            .toObservable()
            .map {
                authRepository.saveUser(it)
                UserResult.Success(it) as UserResult
            }
            .onErrorReturn { UserResult.Failure(it) }
            .startWith(UserResult.Loading)
    }
}