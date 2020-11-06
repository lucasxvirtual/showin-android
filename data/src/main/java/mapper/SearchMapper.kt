package mapper

import br.com.noclaftech.domain.model.Search
import response.SearchResponse
import javax.inject.Inject

class SearchMapper @Inject constructor(private val showMapper: ShowMapper,
                                       private val artistMapper: ArtistMapper,
                                       private val userMapper: UserMapper,
                                       private val categoryMapper: CategoryMapper){

    fun map(response: SearchResponse) : Search {
        return Search(
            shows = showMapper.map(response.shows)!!,
            users = userMapper.map(response.users),
            categories = categoryMapper.map(response.categories)
        )
    }
}