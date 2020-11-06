package repository

import api.ArtistApi
import br.com.noclaftech.domain.model.*
import br.com.noclaftech.domain.repository.ArtistRepository
import io.reactivex.Single
import mapper.*
import storage.Singleton
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val artistApi: ArtistApi,
    private val workedMapper: WorkedMapper,
    private val artistMapper: ArtistMapper,
    private val bankMapper: BankMapper,
    private val messageMapper: MessageMapper,
    private val paginationArtistExtractMapper: PaginationArtistExtractMapper
): ArtistRepository {

    override fun follow(id: Int): Single<Worked> {
        return artistApi.follow(id).map { workedMapper.map(it) }
    }

    override fun unfollow(id: Int): Single<Worked> {
        return artistApi.unfollow(id).map { workedMapper.map(it) }
    }

    override fun saveArtist(artist: Artist): Boolean {
        Singleton.instance.setArtist(artist)
        return true
    }

    override fun postArtist(artisticName: String, biography: String): Single<Artist> {
        return artistApi.postArtist(artisticName, biography).map { artistMapper.map(it) }
    }

//    override fun patchArtist(id: Int, artisticName: String, biography: String): Single<Artist> {
//        return artistApi.patchArtist(id, artisticName, biography).map { artistMapper.map(it) }
//    }

    override fun patchArtist(id: Int, artisticName: String, biography: String, displayOldShows: Boolean, displayFans : Boolean): Single<Artist> {
        return artistApi.patchArtist(id, artisticName, biography, displayOldShows, displayFans ).map { artistMapper.map(it) }
    }

    override fun getArtist(id: Int): Single<Artist> {
        return artistApi.getArtist(id).map { artistMapper.map(it) }
    }

    override fun changeArtistAccount(artisticName: String, bankName: String, owner : String,agency: String, account: String, cpf: String?, cnpj: String?,
            accountType: String): Single<Artist> {
        return artistApi.changeArtistAccount(artisticName, bankName, owner, agency, account, cpf, cnpj, accountType).map { artistMapper.map(it) }
    }

    override fun postMessage(message: String): Single<Message> {
        return artistApi.postMessage(message).map { messageMapper.map(it) }
    }

    override fun putBank(id: Int, owner: String, bank: String, agency: String, account: String, cpf: String?, cnpj: String?,  accountType: String): Single<Bank> {
        return artistApi.putBank(id, owner, bank, agency, account, cpf, cnpj, accountType).map { bankMapper.map(it) }
    }

    override fun getExtract(id: Int): Single<Pagination<ArtistExtract>> {
        return artistApi.getExtract(id).map { paginationArtistExtractMapper.map(it) }
    }

    override fun getBank(): Single<Bank> {
        return artistApi.getBank().map { bankMapper.map(it) }
    }
}