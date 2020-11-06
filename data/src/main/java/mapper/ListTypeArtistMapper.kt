package mapper

import br.com.noclaftech.domain.model.ListTypeArtist
import response.ListTypeArtistResponse
import javax.inject.Inject

class ListTypeArtistMapper @Inject constructor(){
    fun map(responseList: List<ListTypeArtistResponse>?) : List<ListTypeArtist>?{
        if(responseList != null)
            return responseList.map { (map(it)) }
        return null
    }

    private fun map(response: ListTypeArtistResponse) : ListTypeArtist{
        return ListTypeArtist(
            title = response.title,
            type = response.type,
            data = ArtistMapper().map(response.data)
        )
    }
}