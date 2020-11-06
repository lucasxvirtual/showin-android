package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.ArtistResult
import br.com.noclaftech.domain.UserResult
import br.com.noclaftech.domain.model.ChangeArtistAccount
import br.com.noclaftech.domain.repository.ArtistRepository
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class ChangeArtistAccountUseCase @Inject constructor(private val artistRepository: ArtistRepository, private val authRepository: AuthRepository){

    fun execute(changeArtistAccount: ChangeArtistAccount): Observable<UserResult> {
        return artistRepository.changeArtistAccount(changeArtistAccount.artisticName!!, changeArtistAccount.bankName!!, changeArtistAccount.owner!!, changeArtistAccount.agency!!,
            changeArtistAccount.account!!, changeArtistAccount.cpf, changeArtistAccount.cnpj, changeArtistAccount.accountType!!)
            .flatMap {
                authRepository.getUser()
            }
            .toObservable()
            .map {
                authRepository.saveUser(it)
                UserResult.Success(it) as UserResult
            }
            .onErrorReturn { UserResult.Failure(it) }
            .startWith(UserResult.Loading)
    }
}