package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.UnitResult
import br.com.noclaftech.domain.repository.NotificationRepository
import io.reactivex.Observable
import javax.inject.Inject

class DeleteAllNotificationsUseCase @Inject constructor(private val notificationRepository: NotificationRepository) {
    fun execute() : Observable<UnitResult> {
        return notificationRepository.deleteAllNotifications()
            .toObservable()
            .map { UnitResult.Success(it) as UnitResult }
            .onErrorReturn { UnitResult.Failure(it) }
            .startWith(UnitResult.Loading)
    }
}