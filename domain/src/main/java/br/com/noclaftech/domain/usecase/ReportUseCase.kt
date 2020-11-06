package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.ReportResult
import br.com.noclaftech.domain.SearchResult
import br.com.noclaftech.domain.repository.AuthRepository
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class ReportUseCase @Inject constructor(private val authRepository: AuthRepository){

    fun execute(userReported : Int, chatMessage : String, reason : String): Observable<ReportResult> {
        return authRepository.report(userReported, chatMessage, reason)
            .toObservable()
            .map { ReportResult.Success(it) as ReportResult }
            .onErrorReturn { ReportResult.Failure(it) }
            .startWith(ReportResult.Loading)
    }
}