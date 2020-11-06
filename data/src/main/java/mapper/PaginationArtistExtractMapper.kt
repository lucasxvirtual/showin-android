package mapper

import br.com.noclaftech.domain.model.ArtistExtract
import br.com.noclaftech.domain.model.Pagination
import response.ArtistExtractResponse
import response.PaginationResponse
import javax.inject.Inject

class PaginationArtistExtractMapper @Inject constructor(private val artistExtractMapper: ArtistExtractMapper){
    fun map(response : PaginationResponse<ArtistExtractResponse>) : Pagination<ArtistExtract> {
        return Pagination(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = artistExtractMapper.map(response.results)!!
        )
    }
}