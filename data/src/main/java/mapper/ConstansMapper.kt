package mapper

import br.com.noclaftech.domain.model.Constants
import response.ConstantsResponse
import javax.inject.Inject

class ConstansMapper @Inject constructor(){

    fun map(response : List<ConstantsResponse>) : List<Constants>{
        return response.map { (map(it)) }
    }

    fun map(response : ConstantsResponse) : Constants {
        return Constants(
            ageRating = AgeRatingMapper().map(response.ageRating),
            timezones = response.timezones,
            config = ConfigMapper().map(response.config),
            packs = PackMapper().map( response.packs),
            brands = BrandMapper().map( response.brands ),
            categories = CategoryMapper().map(response.categories),
            genders =  GenderMapper().map(response.genders),
            showDurations = ShowDurationMapper().map(response.show_durations)
        )
    }
}