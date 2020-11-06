package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.SearchResult
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val showRepository: ShowRepository){

    fun execute(query: String): Observable<SearchResult> {
        return showRepository.getSearch(query)
            .toObservable()
            .map { SearchResult.Success(it) as SearchResult }
            .onErrorReturn { SearchResult.Failure(it) }
            .startWith(SearchResult.Loading)
    }
}