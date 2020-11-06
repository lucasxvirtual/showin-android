package api

import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleJust
import response.ConstantsResponse
import storage.SessionManager
import javax.inject.Inject

class ConstantsApi @Inject constructor(
    private val constantsEndpoint: ConstantsEndpoint,
    private val sessionManager: SessionManager
){

    fun getConstants() : Single<ConstantsResponse> {
        return constantsEndpoint.getConstants()
    }

    fun getPopup(): Single<String?>{
        return SingleJust(sessionManager.getPopupDate())
    }

    fun setPopup(date: String) {
        sessionManager.setPopupDate(date)
    }
}