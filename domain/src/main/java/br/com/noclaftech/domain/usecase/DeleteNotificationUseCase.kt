package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.UnitResult
import br.com.noclaftech.domain.repository.NotificationRepository
import io.reactivex.Observable
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(private val notificationRepository: NotificationRepository) {

    fun execute(id : Int) : Observable<UnitResult> {
        return notificationRepository.deleteNotification(id)
            .toObservable()
            .map { UnitResult.Success(it) as UnitResult }
            .onErrorReturn { UnitResult.Failure(it) }
            .startWith(UnitResult.Loading)

    }
}