package api

import io.reactivex.Single
import response.WatchResponse
import response.WorkedResponse
import javax.inject.Inject

class WatchApi @Inject constructor(private val watchEndpoint: WatchEndpoint){

    fun watch(id : Int, deviceId: String) : Single<WatchResponse> {
        return  watchEndpoint.watch(id, deviceId)
    }

    fun renewWatch(id : Int, deviceId: String) : Single<WorkedResponse> {
        return  watchEndpoint.renewWatch(id, deviceId)
    }

    fun removeWatch(id : Int, deviceId: String) : Single<WorkedResponse> {
        return  watchEndpoint.removeWatch(id, deviceId)
    }
}