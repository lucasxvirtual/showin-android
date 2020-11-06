package api

import io.reactivex.Single
import response.FollowResponse
import response.PaginationFollowResponse
import javax.inject.Inject

class FollowApi @Inject constructor(private val followApi: FollowEndpoint){
    fun getFollowers(id: Int, page : Int) : Single<PaginationFollowResponse>{
        return followApi.getFollowers(id, page)
    }

    fun getFollowing(id: Int, page : Int) : Single<PaginationFollowResponse>{
        return followApi.getFollowing(id, page)
    }
}