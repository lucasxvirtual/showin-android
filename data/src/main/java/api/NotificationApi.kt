package api

import io.reactivex.Single
import response.PaginationNotificationResponse
import javax.inject.Inject

class NotificationApi @Inject constructor(private val notificationEndpoint: NotificationEndpoint){
    fun getNotification(page : Int) : Single<PaginationNotificationResponse> {
        return notificationEndpoint.getNotification(page)
    }

    fun deleteNotification(id : Int) : Single<Unit>{
        return notificationEndpoint.deleteNotification(id).map { Unit }
    }

    fun deleteAllNotifications(): Single<Unit>{
        return notificationEndpoint.deleteAllNotifications().map { Unit }
    }

}