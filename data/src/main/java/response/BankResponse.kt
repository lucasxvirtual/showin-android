package response

import java.io.Serializable

data class BankResponse(
    val id : Int,
    val owner : String,
    val bank : String,
    val agency : String,
    val account : String,
    val cpf : String?,
    val cnpj : String?,
    val account_type : String

) : Serializable