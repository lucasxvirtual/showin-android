package mapper

import br.com.noclaftech.domain.model.User
import response.UserResponse
import javax.inject.Inject

class UserMapper @Inject constructor(){

    fun map(responseList: List<UserResponse>) : List<User>{
        return responseList.map{(map(it))}
    }

    fun map(response : UserResponse) : User {
        return User(
            id = response.id,
            profileImage = response.profileImage,
            isArtist = response.isArtist,
            isActive = response.isActive,
            balance = response.balance?.let { BalanceMapper().map(it) },
            following = response.following,
            followers = response.followers,
            username  = response.username,
            email = response.email,
            artist = response.artist?.let { ArtistMapper().map(it) },
            userMessages = MessageMapper().map(response.userMessages),
            isFollowed = response.isFollowed,
            lastLogin = response.lastLogin,
            name = response.name,
            gender = response.gender,
            isAdmin = response.isAdmin,
            registerDate = response.registerDate,
            verifiedEmail = response.verifiedEmail,
            birthday = response.birthday,
            allowNotification = response.allowNotification,
            allowArtistEmail = response.allowArtistEmail,
            allowCommercialEmail = response.allowCommercialEmail,
            tokenNotification = response.tokenNotification,
            linkedin = response.linkedin,
            facebook = response.facebook,
            whatsapp = response.whatsapp,
            twitter = response.twitter,
            instagram = response.instagram,
            bio = response.bio,
            timeZone = response.timeZone
        )
    }
}