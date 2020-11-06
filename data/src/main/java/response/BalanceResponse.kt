package response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BalanceResponse(
    val id : Int,
    @SerializedName("balance_owner") val balanceOwner : String?,
    val value : Int?,
    @SerializedName("created_at") val createdAt : String?
)