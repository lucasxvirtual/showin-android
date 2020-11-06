package mapper

import br.com.noclaftech.domain.model.Brand
import response.BrandResponse
import javax.inject.Inject


class BrandMapper @Inject constructor(){

    fun map(response : List<BrandResponse>) : List<Brand>{
        return response.map { (map(it)) }
    }

    fun map(response : BrandResponse) : Brand {
        return Brand(
            id = response.id,
            name = response.name,
            apiName = response.api_name
        )
    }
}