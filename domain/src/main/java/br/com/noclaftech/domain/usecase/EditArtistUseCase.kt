package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.domain.repository.ArtistRepository
import io.reactivex.Observable
import javax.inject.Inject

class EditArtistUseCase @Inject constructor(private val artistRepository: ArtistRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val artist: Artist) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(id: Int, artisticName : String, biography : String, displayOldShows: Boolean, displayFans : Boolean): Observable<Result> {
        return artistRepository.patchArtist(id, artisticName, biography, displayOldShows, displayFans)
            .toObservable()
            .map {
                artistRepository.saveArtist(it)
                Result.Success(it) as Result
            }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}