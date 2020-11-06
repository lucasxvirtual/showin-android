package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.BankResult
import br.com.noclaftech.domain.repository.ArtistRepository
import io.reactivex.Observable
import javax.inject.Inject

class PutBankUseCase @Inject constructor(private val artistRepository: ArtistRepository){

    fun execute(id: Int, owner: String, bank : String, agency : String, account : String, cpf : String?,
                cnpj : String?, accountType: String): Observable<BankResult> {
        return artistRepository.putBank(id, owner, bank, agency, account, cpf, cnpj, accountType)
            .toObservable()
            .map { BankResult.Success(it) as BankResult }
            .onErrorReturn { BankResult.Failure(it) }
            .startWith(BankResult.Loading)
    }
}