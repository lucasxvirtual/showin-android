package api

import io.reactivex.Single

import okhttp3.ResponseBody
import response.PaginationNotificationResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationEndpoint{
    @GET("notificationapp/notification")
    fun getNotification(@Query("page") page : Int) : Single<PaginationNotificationResponse>

    @DELETE("notificationapp/notification/{id}/")
    fun deleteNotification(@Path("id") id: Int) : Single<ResponseBody>

    @DELETE("notificationapp/notification/delete-all/")
    fun deleteAllNotifications() : Single<ResponseBody>

}
