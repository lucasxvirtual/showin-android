package br.com.noclaftech.domain.model

import java.io.Serializable

//Apenas para o fluxo de mudar para conta de artista
class ChangeArtistAccount : Serializable {
    var artisticName: String? = null
    var biography: String? = null
    var bankName: String? = null
    var owner : String? = null
    var agency: String? = null
    var account: String? = null
    var cpf: String? = null
    var cnpj: String? = null
    var accountType: String? = null
    var displayOldShows: Boolean? = null
    var displayFans: Boolean? = null
}