package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.ArtistExtractResult
import br.com.noclaftech.domain.repository.ArtistRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetArtistExtractUseCase @Inject constructor(private val artistRepository: ArtistRepository) {

    fun execute(id : Int): Observable<ArtistExtractResult> {
        return artistRepository.getExtract(id)
            .toObservable()
            .map {
                ArtistExtractResult.Success(it) as ArtistExtractResult
            }
            .onErrorReturn { ArtistExtractResult.Failure(it) }
            .startWith(ArtistExtractResult.Loading)
    }
}