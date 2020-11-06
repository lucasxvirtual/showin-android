package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.ArtistResult
import br.com.noclaftech.domain.repository.ArtistRepository
import io.reactivex.Observable
import javax.inject.Inject


class CreateArtistUseCase @Inject constructor(private val artistRepository: ArtistRepository){

    fun execute(artisticName : String, biography : String): Observable<ArtistResult> {
        return artistRepository.postArtist(artisticName, biography)
            .toObservable()
            .map {
                //todo: decide what will be done with the created artist
//                userRepository.saveUser(it)
                ArtistResult.Success(it) as ArtistResult
            }
            .onErrorReturn { ArtistResult.Failure(it) }
            .startWith(ArtistResult.Loading)
    }
}