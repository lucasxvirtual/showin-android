package mapper

import br.com.noclaftech.domain.model.PaginationFollow
import response.PaginationFollowResponse
import javax.inject.Inject

class PaginationFollowMapper @Inject constructor(){

    fun map(response : PaginationFollowResponse) : PaginationFollow {
        return PaginationFollow(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = FollowMapper().map(response.results)
        )
    }
}