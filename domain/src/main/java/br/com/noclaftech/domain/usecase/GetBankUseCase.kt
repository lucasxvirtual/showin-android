package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.BankResult
import br.com.noclaftech.domain.repository.ArtistRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetBankUseCase @Inject constructor(private val artistRepository: ArtistRepository){

    fun execute(): Observable<BankResult> {
        return artistRepository.getBank()
            .toObservable()
            .map { BankResult.Success(it) as BankResult }
            .onErrorReturn { BankResult.Failure(it) }
            .startWith(BankResult.Loading)
    }
}