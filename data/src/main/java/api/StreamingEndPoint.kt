package api

import io.reactivex.Single
import response.LiveConfigResponse
import response.WorkedResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StreamingEndPoint {

    @POST("/showapp/show/{id}/live/")
    fun goLive(@Path("id") id : Int) : Single<WorkedResponse>

    @POST("/showapp/show/{id}/finish/")
    fun finish(@Path("id") id : Int) : Single<WorkedResponse>

    @GET("/showapp/show/{id}/config/")
    fun getConfig(@Path("id") id: Int) : Single<LiveConfigResponse>

}