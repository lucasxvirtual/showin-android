package response

import java.io.Serializable

data class GenderResponse(
    val id : Int,
    val name : String,
    val value : String
) : Serializable