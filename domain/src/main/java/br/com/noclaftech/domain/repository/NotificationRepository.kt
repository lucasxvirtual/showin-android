package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.PaginationNotification
import io.reactivex.Single

interface NotificationRepository {
    fun getNotification(page : Int) : Single<PaginationNotification>
    fun deleteNotification(id : Int) : Single<Unit>
    fun deleteAllNotifications(): Single<Unit>
}