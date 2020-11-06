package mapper

import br.com.noclaftech.domain.model.Contact
import response.ContactResponse
import javax.inject.Inject

class ContactMapper @Inject constructor(){

    fun map(response : List<ContactResponse>) : List<Contact>{
        return response.map { (map(it)) }
    }

    fun map(response : ContactResponse) : Contact {
        return Contact(
            id = response.id,
            title = response.title,
            message = response.message,
            created_at = response.created_at,
            user = response.user
        )
    }
}