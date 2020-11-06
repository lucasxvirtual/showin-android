package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.model.Worked
import br.com.noclaftech.domain.repository.DonateRepository
import helper.Helper
import io.reactivex.Observable
import javax.inject.Inject

class DonateUseCase @Inject constructor(private val donateRepository: DonateRepository){
    sealed class Result {
        object Loading : Result()
        data class Success(val worked: Worked) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    fun execute(id : Int, value : Int, password : String, user: String? = null, show: Int? = null): Observable<Result> {
        return if(user != null)
            donateRepository.postDonate(id, user, value,  Helper.convertPassMd5(password))
                .toObservable()
                .map {Result.Success(it) as Result}
                .onErrorReturn { Result.Failure(it) }
                .startWith(Result.Loading)
        else
            donateRepository.postDonateShow(id, show!!, value, Helper.convertPassMd5(password))
                .toObservable()
                .map {Result.Success(it) as Result}
                .onErrorReturn { Result.Failure(it) }
                .startWith(Result.Loading)

    }
}