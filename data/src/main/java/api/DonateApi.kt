package api

import io.reactivex.Single
import response.PaginationExtractResponse
import response.WorkedResponse
import javax.inject.Inject

class DonateApi @Inject constructor(private val donateEndpoint: DonateEndpoint){
    fun postDonate(id : Int, user : String, value : Int, password : String) : Single<WorkedResponse>{
        return donateEndpoint.postBalance(id, user, value, password)
    }

    fun postDonateShow(id: Int, show: Int, value: Int, password: String) : Single<WorkedResponse>{
        return donateEndpoint.postDonateShow(id, show, value, password)
    }

    fun getExtract(page : Int) : Single<PaginationExtractResponse>{
        return donateEndpoint.getExtract(page)
    }
}