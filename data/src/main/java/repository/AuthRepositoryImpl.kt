package repository

import api.AuthenticateApi
import br.com.noclaftech.domain.model.*
import br.com.noclaftech.domain.repository.AuthRepository
import io.reactivex.Single
import mapper.AuthMapper
import mapper.ContactMapper
import mapper.ReportMapper
import mapper.WorkedMapper
import response.UserResponse
import storage.SessionManager
import storage.Singleton
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi : AuthenticateApi,
    private val authMapper: AuthMapper,
    private val workedMapper: WorkedMapper,
    private val contactMapper: ContactMapper,
    private val reportMapper: ReportMapper,
    private val session : SessionManager
): AuthRepository {

    override fun auth(username : String, password : String): Single<Token> {
        return authApi.auth(username, password).map { authMapper.map((it)) }
    }

    override fun getToken(): String? {
        return session.getToken()
    }

    override fun saveToken(token: String, remember : Boolean) {
        session.saveToken(token)
        saveRemember(remember)
    }

    override fun getUser(): Single<User> {
//        return if (Singleton.instance.getUser() != null)
//            Singleton.instance.getUser()!!
//        else
            return authApi.getUser().map { authMapper.map(it) }
    }

    override fun getUser(id: Int): Single<User> {
        return authApi.getUser(id).map {authMapper.map(it)}
    }

    override fun follow(id: Int): Single<Worked> {
        return authApi.follow(id).map { workedMapper.map(it) }
    }

    override fun saveUser(user: User) : Boolean {
        Singleton.instance.setUser(user)
        return true
    }

    override fun saveRemember(boolean: Boolean) {
        session.saveRemember(boolean)
    }

    override fun getRemember(): Boolean {
        return session.getRemember()
    }

    override fun putImage(id: Int, image: String): Single<User> {
        return authApi.putImage(id, image).map { authMapper.map(it) }
    }

    override fun putUser(id: Int, name: String, birthday: String, gender: String, biography : String, artisticName: String?): Single<User> {
        return authApi.putUser(id, name, birthday, gender, biography, artisticName).map { authMapper.map(it) }
    }

    override fun postUser(username: String, email: String,name: String, birthday: String, password: String, gender: String): Single<User> {
        return authApi.postUser(username, email, name, birthday, password, gender).map { authMapper.map(it) }
    }

    override fun putUser(id: Int, allow_notification: Boolean, allow_artist_email: Boolean, allow_commercial_email: Boolean): Single<User> {
        return authApi.putUser(id, allow_notification, allow_artist_email, allow_commercial_email).map { authMapper.map(it) }
    }

    override fun putUser(id: Int, token: String): Single<User> {
        return authApi.putUser(id, token).map { authMapper.map(it) }
    }

    override fun contact(title: String, message: String): Single<Contact> {
        return authApi.contact(title, message).map { contactMapper.map(it) }
    }

    override fun authSocialNetwork(accessToken: String, socialNetwork: String): Single<Token> {
        return authApi.authSocialNetwork(accessToken, socialNetwork).map { authMapper.map(it) }
    }

    override fun forgotPassword (email: String): Single<Worked> {
        return authApi.forgotPassword(email).map { workedMapper.map(it) }
    }

    override fun putPassword(id: Int, passwordOld: String, passwordNew: String): Single<User>{
        return authApi.putPassword(id, passwordOld, passwordNew).map { authMapper.map(it)}
    }

    override fun putSocials(id: Int, whatsapp : String?, instagram : String?, facebook : String?, linkedin : String?, twitter : String?): Single<User>{
        return authApi.putSocials(id, whatsapp, instagram, facebook, linkedin, twitter).map { authMapper.map(it) }
    }

    override fun report(userReported: Int, chatMessage: String, reason: String): Single<Report> {
        return authApi.report(userReported, chatMessage, reason).map { reportMapper.map(it) }
    }
    override fun logout() {
        session.logout()
    }
}