package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.ContactResult
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class ContactUseCase @Inject constructor(private val authRepository: AuthRepository){

    fun execute(title : String, message : String): Observable<ContactResult> {
        return authRepository.contact(title, message)
            .toObservable()
            .map {ContactResult.Success(it) as ContactResult}
            .onErrorReturn { ContactResult.Failure(it) }
            .startWith(ContactResult.Loading)
    }
}