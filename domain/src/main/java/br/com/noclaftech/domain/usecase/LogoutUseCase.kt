package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val authRepository: AuthRepository){

    fun execute() {
        return authRepository.logout()
    }
}