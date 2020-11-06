package mapper

import br.com.noclaftech.domain.model.Message
import br.com.noclaftech.domain.model.Notification
import response.MessageResponse
import response.NotificationResponse
import javax.inject.Inject

class NotificationMapper @Inject constructor(){
    fun map(responseList: List<NotificationResponse>) : List<Notification>{
        return responseList.map{(map(it))}
    }

    fun map(response : NotificationResponse) : Notification {
        return Notification(
            id = response.id,
            title = response.title,
            message = response.message,
            createdAt = response.created_at,
            user = response.user
        )
    }
}