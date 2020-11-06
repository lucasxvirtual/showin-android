package mapper

import br.com.noclaftech.domain.model.Message
import response.MessageResponse
import javax.inject.Inject

class MessageMapper @Inject constructor(){
    fun map(responseList: List<MessageResponse>?) : List<Message>?{
        if (responseList == null)
            return null
        return responseList.map{(map(it))}
    }

    fun map(response : MessageResponse) : Message {
        return Message(
            id = response.id,
            message = response.message,
            created_at = response.created_at,
            artist = response.artist
        )
    }
}