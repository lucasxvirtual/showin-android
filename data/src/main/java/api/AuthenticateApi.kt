package api

import io.reactivex.Single
import response.*
import javax.inject.Inject

class AuthenticateApi @Inject constructor(private val authEndpoint: AuthenticateEndPoint){
    fun auth(username : String, password : String) : Single<TokenResponse> {
        return authEndpoint.auth(username, password)
    }

    fun getUser() : Single<UserResponse>{
        return authEndpoint.getUser()
    }

    fun getUser(id: Int) : Single<UserResponse>{
        return authEndpoint.getUser(id)
    }

    fun follow(id : Int) : Single<WorkedResponse>{
        return authEndpoint.postFollow(id)
    }

    fun putImage(id : Int, profileImage : String) : Single<UserResponse>{
        return  authEndpoint.putImage(id, profileImage)
    }

    fun putUser(id: Int, name : String, birthday : String, gender : String, biography : String, artisticName: String?) : Single<UserResponse>{
        return authEndpoint.putUser(id, name, birthday, gender, biography, artisticName)
    }

    fun putPassword(id: Int, passwordOld: String, passwordNew: String) : Single<UserResponse>{
        return authEndpoint.putPassword(id, passwordOld, passwordNew)
    }

    fun postUser(username: String, email : String, name : String, birthday: String, password: String, gender: String) : Single<UserResponse>{
        return authEndpoint.postUser(username, email, name, birthday, password, gender)
    }

    fun putUser(id: Int, allow_notification : Boolean, allow_artist_email : Boolean, allow_commercial_email : Boolean) : Single<UserResponse>{
        return authEndpoint.putUser(id, allow_notification, allow_artist_email, allow_commercial_email)
    }

    fun putUser(id: Int, token : String): Single<UserResponse>{
        return authEndpoint.putUser(id, token)
    }

    fun contact(title : String, message : String) : Single<ContactResponse>{
        return authEndpoint.contact(title, message)
    }

    fun authSocialNetwork(accessToken: String, socialNetwork: String) : Single<TokenResponse> {
        return authEndpoint.authSocialNetwork(socialNetwork, accessToken)
    }

    fun forgotPassword(email: String) : Single<WorkedResponse> {
        return authEndpoint.forgotPassword(email)
    }

    fun putSocials(id: Int, whatsapp : String?, instagram : String?, facebook : String?, linkedin : String?, twitter : String?): Single<UserResponse>{
        return authEndpoint.putSocials(id, whatsapp, instagram, facebook, linkedin, twitter)
    }

    fun report(userReported : Int, chatMessage : String, reason : String) : Single<ReportResponse>{
        return authEndpoint.report(userReported, chatMessage, reason)
    }
}