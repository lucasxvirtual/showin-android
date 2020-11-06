package mapper

import br.com.noclaftech.domain.model.Gender
import br.com.noclaftech.domain.model.ShowDuration
import response.GenderResponse
import response.ShowDurationResponse
import javax.inject.Inject

class ShowDurationMapper @Inject constructor(){

    fun map(response : List<ShowDurationResponse>) : List<ShowDuration>{
        return response.map { (map(it)) }
    }

    fun map(response : ShowDurationResponse) : ShowDuration {
        return ShowDuration(
            id = response.id,
            name = response.name,
            minutes = response.minutes
        )
    }
}