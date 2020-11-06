package mapper

import br.com.noclaftech.domain.model.ShowAndArtist
import response.ShowAndArtistResponse
import javax.inject.Inject


class ShowAndArtistMapper @Inject constructor(private val bannerMapper: BannerMapper){

    fun map(response: ShowAndArtistResponse) : ShowAndArtist{
        return ShowAndArtist(
            shows = ListTypeShowMapper().map(response.shows),
            artists = ListTypeArtistMapper().map(response.artists),
            banners = bannerMapper.map(response.banners)
        )
    }
}