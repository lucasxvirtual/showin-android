package api

import io.reactivex.Single
import response.WatchResponse
import response.WorkedResponse
import retrofit2.http.*

interface WatchEndpoint {

    @GET("/showapp/show/{id}/watch/")
    fun watch(@Path ("id") id : Int,
              @Query("uuid") deviceId: String): Single<WatchResponse>

    @FormUrlEncoded
    @POST("/showapp/show/{id}/renew-watch/")
    fun renewWatch(@Path ("id") id : Int,
                   @Field("uuid") deviceId: String): Single<WorkedResponse>

    @FormUrlEncoded
    @POST("/showapp/show/{id}/remove-watch/")
    fun removeWatch(@Path ("id") id : Int,
                    @Field("uuid") deviceId: String): Single<WorkedResponse>
}