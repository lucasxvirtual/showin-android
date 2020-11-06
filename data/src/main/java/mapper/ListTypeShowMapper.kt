package mapper

import br.com.noclaftech.domain.model.ListTypeShow
import response.ListTypeShowResponse
import javax.inject.Inject

class ListTypeShowMapper @Inject constructor(){
    fun map(responseList: List<ListTypeShowResponse>) : List<ListTypeShow>{
        return responseList.map { (map(it)) }
    }

    private fun map(response: ListTypeShowResponse) : ListTypeShow{
        return ListTypeShow(
            title = response.title,
            type = response.type,
            data = ShowMapper().map(response.data)!!
        )
    }
}