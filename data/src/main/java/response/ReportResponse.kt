package response

import java.io.Serializable

data class ReportResponse(
    val id : Int,
    val chat_message : String,
    val reason : String,
    val created_at : String,
    val user_reported : Int,
    val user_reporting : Int
) : Serializable