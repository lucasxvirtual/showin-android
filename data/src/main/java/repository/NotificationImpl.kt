package repository

import api.NotificationApi
import br.com.noclaftech.domain.model.PaginationNotification
import br.com.noclaftech.domain.repository.NotificationRepository
import io.reactivex.Single
import mapper.PaginationNotificationMapper
import javax.inject.Inject

class NotificationImpl @Inject constructor(
    private val notificationApi: NotificationApi,
    private val paginationNotificationMapper: PaginationNotificationMapper
) : NotificationRepository {

    override fun getNotification(page: Int): Single<PaginationNotification> {
        return notificationApi.getNotification(page).map { paginationNotificationMapper.map(it) }
    }

    override fun deleteNotification(id: Int): Single<Unit>{
        return notificationApi.deleteNotification(id)
    }

    override fun deleteAllNotifications(): Single<Unit>{
        return notificationApi.deleteAllNotifications()
    }
}