package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.ShowAndArtist
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetShowAndArtistUseCase @Inject constructor(private val showRepository: ShowRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val showAndArtist: ShowAndArtist) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(): Observable<Result> {
        return showRepository.getShowAndArtist()
            .toObservable()
            .map { Result.Success(it) as Result }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}