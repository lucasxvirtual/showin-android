package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.ShowListResult
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class ShowUseCase @Inject constructor(private val showRepository: ShowRepository){

    fun execute(id: Int): Observable<ShowListResult> {

        return showRepository.getCategoryShows(id)
            .toObservable()
            .map { ShowListResult.Success(it) as ShowListResult }
            .onErrorReturn { ShowListResult.Failure(it) }
            .startWith(ShowListResult.Loading)
    }
}