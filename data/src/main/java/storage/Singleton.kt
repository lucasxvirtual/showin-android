package storage

import br.com.noclaftech.domain.model.Artist
import br.com.noclaftech.domain.model.Bank
import br.com.noclaftech.domain.model.Constants
import br.com.noclaftech.domain.model.User
import io.reactivex.Single

class Singleton private constructor(){

    private object HOLDER {
        val INSTANCE = Singleton()
    }

    companion object{
        val instance  : Singleton by lazy {  HOLDER.INSTANCE }
        private var user : Single<User>? = null
        private var artist : Single<Artist>? = null
        private var constants : Single<Constants>? = null
    }

    fun setUser(user: User){
        Companion.user = Single.just(user)
    }

    fun getUser() = user

    fun clearUser(){
        user = null
    }

    fun setArtist(artist: Artist){
        Companion.artist = Single.just(artist)
    }

    fun getArtist(): Single<Artist?>?{
        if(getUser()?.blockingGet()?.artist == null){
            return null
        }
        return Single.just(getUser()?.blockingGet()?.artist)
    }

    fun clearArtist(){
        artist = null
    }

    fun setConstants(constants: Constants){
        Companion.constants = Single.just(constants)
    }

    fun getConstants() = constants
}