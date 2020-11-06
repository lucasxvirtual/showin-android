package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.MessageResult
import br.com.noclaftech.domain.model.Message
import br.com.noclaftech.domain.repository.ArtistRepository
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class PostMessageUseCase @Inject constructor(private val artistRepository: ArtistRepository){

    fun execute(message : String): Observable<MessageResult> {
        return artistRepository.postMessage(message)
            .toObservable()
            .map { MessageResult.Success(it) as MessageResult }
            .onErrorReturn { MessageResult.Failure(it) }
            .startWith(MessageResult.Loading)
    }
}