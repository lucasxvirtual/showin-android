package mapper

import br.com.noclaftech.domain.model.Banner
import response.BannerResponse
import javax.inject.Inject

class BannerMapper @Inject constructor(private val showMapper: ShowMapper){
    fun map(responseList: List<BannerResponse>?) : List<Banner>?{
        if (responseList == null)
            return null

        return responseList.map{(map(it)!!)}
    }

    fun map(response : BannerResponse?) : Banner? {
        if (response == null)
            return null

        return Banner(
            link = response.link,
            show = response.show?.let { showMapper.map(it) },
            image = response.image
        )
    }
}