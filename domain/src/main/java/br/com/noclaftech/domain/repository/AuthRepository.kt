package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.*
import io.reactivex.Single

interface AuthRepository{
    fun auth(username : String, password : String) : Single<Token>

    fun getToken() : String?

    fun saveToken(token : String, remember : Boolean = false)

    fun getUser() : Single<User>

    fun getUser(id: Int) : Single<User>

    fun follow(id: Int): Single<Worked>

    fun saveUser(user: User) : Boolean

    fun saveRemember(boolean: Boolean)

    fun getRemember() : Boolean

    fun putImage(id : Int, image : String) : Single<User>

    fun putUser(id : Int, name : String, birthday : String, gender : String, biography : String, artisticName: String?) : Single<User>

    fun forgotPassword (email: String): Single<Worked>

    fun putPassword(id: Int, passwordOld: String, passwordNew: String): Single<User>

    fun postUser(username: String, email : String, name: String, birthday: String, password: String, gender: String) : Single<User>

    fun putUser(id : Int, allow_notification : Boolean, allow_artist_email : Boolean, allow_commercial_email : Boolean) : Single<User>

    fun putUser(id : Int, token: String) : Single<User>

    fun contact(title : String, message : String) : Single<Contact>

    fun authSocialNetwork(accessToken: String, socialNetwork: String) : Single<Token>

    fun putSocials(id: Int, whatsapp : String?, instagram : String?, facebook : String?, linkedin : String?, twitter : String?): Single<User>

    fun report(userReported : Int, chatMessage : String, reason : String) : Single<Report>

    fun logout()
}