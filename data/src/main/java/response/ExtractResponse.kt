package response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExtractResponse(
    val id : Int,
    @SerializedName("balance_from") val balanceFrom : BalanceResponse?,
    @SerializedName("balance_to") val balanceTo : BalanceResponse?,
    val reason : String,
    @SerializedName("is_withdrawal") val isWithdrawal : Boolean,
    val value : Int?,
    @SerializedName("created_at") val createdAt : String?,
    val price : Float? = null
): Serializable