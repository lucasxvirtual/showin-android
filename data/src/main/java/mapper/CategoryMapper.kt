package mapper

import br.com.noclaftech.domain.model.Category
import response.CategoryResponse
import javax.inject.Inject

class CategoryMapper @Inject constructor(){

    fun map(response : List<CategoryResponse>) : List<Category>{
        return response.map { map(it) }
    }

    fun map(response : CategoryResponse) : Category {
        return Category(
            id = response.id,
            name = response.name,
            ecad = response.ecad
        )
    }
}