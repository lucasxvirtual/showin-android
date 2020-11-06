package api

import io.reactivex.Single
import response.UserResponse
import javax.inject.Inject

class UserApi @Inject constructor(private val userEndpoint : AuthenticateEndPoint){
    fun getUser() : Single<UserResponse>{
        return  userEndpoint.getUser()
    }
}