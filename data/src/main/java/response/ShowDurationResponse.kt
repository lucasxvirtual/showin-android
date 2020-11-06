package response

import java.io.Serializable

data class ShowDurationResponse(
    val id : Int,
    val name : String,
    val minutes : Int
) : Serializable