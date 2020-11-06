package mapper

import br.com.noclaftech.domain.model.Artist
import response.ArtistResponse
import javax.inject.Inject

class ArtistMapper @Inject constructor(){
    fun map(responseList: List<ArtistResponse>) : List<Artist>{
        return responseList.map{(map(it))}
    }

    fun map(response : ArtistResponse) : Artist {
        return Artist(
            id = response.id,
            user = response.user?.let { UserMapper().map(it) },
            nextShows = ShowMapper().map(response.nextShows),
            oldShows = ShowMapper().map(response.oldShows),
            bank = BankMapper().map(response.bank),
            artisticName = response.artisticName,
            verified = response.verified,
            createdAt = response.createdAt,
            lives = response.lives,
            testNextShows = ShowMapper().map(response.testNextShows),
            testOldShows = ShowMapper().map(response.testOldShows)
//            biography = response.biography,
//            displayOldShows = response.displayOldShows,
//            displayFans = response.displayFans,
//            isFollowed = response.isFollowed,
//            followingNumber = response.followingNumber,
//            followersNumber = response.followersNumber,
//            artistMessage = MessageMapper().map(response.artistMessage)
        )
    }
}