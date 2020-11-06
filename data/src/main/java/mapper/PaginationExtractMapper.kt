package mapper

import br.com.noclaftech.domain.model.PaginationExtract
import response.PaginationExtractResponse
import javax.inject.Inject

class PaginationExtractMapper @Inject constructor(private val extractMapper: ExtractMapper){

    fun map(response : PaginationExtractResponse) : PaginationExtract {
        return PaginationExtract(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = extractMapper.map(response.results)
        )
    }
}