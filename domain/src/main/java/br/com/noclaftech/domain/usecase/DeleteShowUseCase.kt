package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.UnitResult
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class DeleteShowUseCase @Inject constructor(private val showRepository: ShowRepository) {

    fun execute(id : Int) : Observable<UnitResult> {
        return showRepository.deleteShow(id)
            .toObservable()
            .map { UnitResult.Success(it) as UnitResult }
            .onErrorReturn { UnitResult.Failure(it) }
            .startWith(UnitResult.Loading)

    }
}