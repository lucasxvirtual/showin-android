package mapper

import br.com.noclaftech.domain.model.Follow
import br.com.noclaftech.domain.model.Gender
import response.FollowResponse
import response.GenderResponse
import javax.inject.Inject

class GenderMapper @Inject constructor(){

    fun map(response : List<GenderResponse>) : List<Gender>{
        return response.map { (map(it)) }
    }

    fun map(response : GenderResponse) : Gender {
        return Gender(
            id = response.id,
            name = response.name,
            value = response.value
        )
    }
}