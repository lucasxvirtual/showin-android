package mapper

import br.com.noclaftech.domain.model.Pagination
import br.com.noclaftech.domain.model.Show
import response.PaginationResponse
import response.ShowResponse
import javax.inject.Inject

class PaginationShowMapper @Inject constructor(private val showMapper: ShowMapper) {

    fun map(response : PaginationResponse<ShowResponse>) : Pagination<Show> {
        return Pagination(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = showMapper.map(response.results)!!
        )
    }

}