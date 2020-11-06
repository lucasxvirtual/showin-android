package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class RememberUseCase @Inject constructor(private val authRepository: AuthRepository, private val userUseCase: UserUseCase){

    fun execute(): Observable<UserUseCase.Result>? {

        if(authRepository.getRemember() && authRepository.getToken() != null){
            return userUseCase.execute()
        }
        else{
            authRepository.logout()
        }
        return null
    }
}