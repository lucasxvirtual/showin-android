package mapper

import br.com.noclaftech.domain.model.Pack
import response.PackResponse
import javax.inject.Inject

class PackMapper @Inject constructor(){
    fun map(responseList: List<PackResponse>) : List<Pack>{
        return responseList.map { (map(it)) }
    }

    fun map(response: PackResponse) : Pack {
        return Pack(
            id = response.id,
            coin_amout = response.coin_amout,
            price = response.price,
            created_at = response.created_at
        )
    }
}