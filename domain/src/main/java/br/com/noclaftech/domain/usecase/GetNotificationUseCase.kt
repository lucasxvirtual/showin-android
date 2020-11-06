package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.NotificationResult
import br.com.noclaftech.domain.repository.NotificationRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(private val notificationRepository: NotificationRepository){
    fun execute(page : Int): Observable<NotificationResult> {
        return notificationRepository.getNotification(page)
            .toObservable()
            .map { NotificationResult.Success(it) as NotificationResult }
            .onErrorReturn { NotificationResult.Failure(it) }
            .startWith(NotificationResult.Loading)
    }
}