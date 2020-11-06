package mapper

import br.com.noclaftech.domain.model.AgeRating
import response.AgeRatingResponse
import javax.inject.Inject

class AgeRatingMapper @Inject constructor(){
    fun map(responseList: List<AgeRatingResponse>) : List<AgeRating>{
        return responseList.map{(map(it))}
    }

    fun map(response : AgeRatingResponse) : AgeRating {
        return AgeRating(
            id = response.id,
            age = response.age,
            image = response.image
        )
    }

}