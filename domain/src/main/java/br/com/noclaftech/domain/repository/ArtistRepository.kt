package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.*
import io.reactivex.Single

interface ArtistRepository {

    fun follow(id: Int): Single<Worked>

    fun unfollow(id: Int): Single<Worked>

    fun saveArtist(artist: Artist) : Boolean

    fun postArtist(artisticName: String, biography: String): Single<Artist>

    fun patchArtist(id: Int, artisticName: String, biography: String, displayOldShows: Boolean, displayFans : Boolean): Single<Artist>

    fun getArtist(id : Int) : Single<Artist>

    fun changeArtistAccount(artisticName : String, bankName : String, owner : String, agency : String, account : String,
                            cpf : String?,cnpj : String?, accountType : String) : Single<Artist>

    fun postMessage(message : String) : Single<Message>

    fun putBank(id: Int, owner: String, bank : String, agency : String, account : String, cpf : String?, cnpj : String?, accountType: String) : Single<Bank>

    fun getExtract(id: Int) : Single<Pagination<ArtistExtract>>

    fun getBank() : Single<Bank>
}