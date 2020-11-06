package api

import io.reactivex.Single
import response.FollowResponse
import response.PaginationFollowResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FollowEndpoint{
    @GET("userapp/user/{id}/followers")
    fun getFollowers(@Path("id") id: Int,
                     @Query("page") page : Int) : Single<PaginationFollowResponse>

    @GET("userapp/user/{id}/following")
    fun getFollowing(@Path("id") id : Int,
                     @Query ("page") page : Int) : Single<PaginationFollowResponse>
}