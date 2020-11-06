package mapper

import br.com.noclaftech.domain.model.Follow
import response.FollowResponse
import javax.inject.Inject

class FollowMapper @Inject constructor(){

    fun map(response : List<FollowResponse>) : List<Follow>{
        return response.map { (map(it)) }
    }

    fun map(response : FollowResponse) : Follow {
        return Follow(
            id = response.id,
            username = response.username,
            profileImage = response.profile_image,
            artist = response.artist
        )
    }
}