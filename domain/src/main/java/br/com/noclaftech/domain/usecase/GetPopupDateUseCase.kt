package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.StringResult
import br.com.noclaftech.domain.repository.ConstantsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetPopupDateUseCase @Inject constructor(private val constantsRepository: ConstantsRepository){

    fun execute(): Observable<StringResult> {
        return constantsRepository.getPopupDate()
            .toObservable()
            .map {
                StringResult.Success(it) as StringResult
            }
            .onErrorReturn { StringResult.Failure(it) }
            .startWith(StringResult.Loading)
    }
}