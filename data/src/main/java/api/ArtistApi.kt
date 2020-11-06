package api

import io.reactivex.Single
import response.*
import javax.inject.Inject

class ArtistApi @Inject constructor(private val artistEndPoint: ArtistEndPoint){
    fun follow(id: Int) : Single<WorkedResponse> {
        return artistEndPoint.postFollow(id)
    }

    fun unfollow(id: Int) : Single<WorkedResponse> {
        return artistEndPoint.postUnfollow(id)
    }

    fun postArtist(artisticName: String, biography: String): Single<ArtistResponse> {
        return artistEndPoint.postArtist(artisticName, biography)
    }

    fun patchArtist(id: Int, artisticName: String, biography: String, displayOldShows: Boolean, displayFans : Boolean): Single<ArtistResponse> {
        return artistEndPoint.patchArtist(id, artisticName, biography, displayOldShows, displayFans)
    }

    fun getArtist(id : Int) : Single<ArtistResponse>{
        return artistEndPoint.getArtist(id)
    }

    fun changeArtistAccount(artisticName : String, bankName : String, owner : String, agency : String, account : String,
                            cpf : String?, cnpj : String?, accountType : String) : Single<ArtistResponse>{
        return artistEndPoint.changeArtistAccount(artisticName, bankName, owner, agency, account, cpf, cnpj, accountType)
    }

    fun postMessage(message : String) : Single<MessageResponse>{
        return artistEndPoint.postMessage(message)
    }

    fun putBank(id: Int, owner: String, bank : String, agency : String, account : String, cpf : String?,
                cnpj : String?, accountType: String) : Single<BankResponse>{
        return artistEndPoint.putBank(id, owner, bank, agency, account, cpf, cnpj, accountType)
    }

    fun getExtract(id: Int): Single<PaginationResponse<ArtistExtractResponse>> {
        return artistEndPoint.getExtractArtist(id)
    }

    fun getBank(): Single<BankResponse> {
        return artistEndPoint.getBank()
    }
}