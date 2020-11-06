package mapper

import br.com.noclaftech.domain.model.PaginationNotification
import response.PaginationNotificationResponse
import javax.inject.Inject

class PaginationNotificationMapper @Inject constructor(){

    fun map(response : PaginationNotificationResponse) : PaginationNotification {
        return PaginationNotification(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = NotificationMapper().map(response.results)
        )
    }
}