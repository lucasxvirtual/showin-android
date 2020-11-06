package api

import io.reactivex.Single
import response.ConstantsResponse
import retrofit2.http.GET

interface ConstantsEndpoint {

    @GET("userapp/constants/")
    fun getConstants() : Single<ConstantsResponse>

}