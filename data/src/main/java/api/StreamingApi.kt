package api

import io.reactivex.Single
import response.LiveConfigResponse
import response.WorkedResponse
import javax.inject.Inject

class StreamingApi @Inject constructor(private val streamingEndPoint: StreamingEndPoint){
    fun goLive(id: Int) : Single<WorkedResponse> {
        return streamingEndPoint.goLive(id)
    }

    fun finish(id: Int) : Single<WorkedResponse> {
        return streamingEndPoint.finish(id)
    }

    fun getConfig(id: Int) : Single<LiveConfigResponse>{
        return streamingEndPoint.getConfig(id)
    }
}