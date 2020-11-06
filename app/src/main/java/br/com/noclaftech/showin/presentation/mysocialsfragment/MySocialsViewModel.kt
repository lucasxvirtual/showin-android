package br.com.noclaftech.showin.presentation.mysocialsfragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.showin.presentation.BaseViewModel
import java.io.Serializable
import javax.inject.Inject

class MySocialsViewModel @Inject constructor(application: Application) : BaseViewModel(application) {
    val user = MutableLiveData<User?>().apply { value = null }
    val whatsapp = MutableLiveData<String?>().apply { value = null }
    val instagram = MutableLiveData<String?>().apply { value = null }
    val facebook = MutableLiveData<String?>().apply { value = null }
    val linkedin = MutableLiveData<String?>().apply { value = null }
    val twitter = MutableLiveData<String?>().apply { value = null }

    private val openWhatsapp = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenWhatsapp(): LiveData<Boolean> = openWhatsapp

    private val openFacebook = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenFacebook(): LiveData<Boolean> = openFacebook

    private val openInstagram = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenInstagram(): LiveData<Boolean> = openInstagram

    private val openLinkedin = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenLinkedin(): LiveData<Boolean> = openLinkedin

    private val openTwitter = MutableLiveData<Boolean>().apply { value = false }
    fun getOpenTwitter(): LiveData<Boolean> = openTwitter

    fun bound(serializable: Serializable){
        user.value  =  serializable as User

        whatsapp.postValue(user.value?.whatsapp)
        instagram.postValue(user.value?.instagram)
        facebook.postValue(user.value?.facebook)
        linkedin.postValue(user.value?.linkedin)
        twitter.postValue(user.value?.twitter)

        val whatsapp = user.value?.whatsapp
        val instagram =  user.value?.instagram
        val facebok = user.value?.facebook
        val linkedin = user.value?.linkedin
        val twitter = user.value?.twitter

        if ( whatsapp == null )
            this.whatsapp.postValue("")

        if ( instagram == null )
            this.instagram.postValue("")

        if ( facebok == null )
            this.facebook.postValue("")

        if ( linkedin == null )
            this.linkedin.postValue("")

        if ( twitter == null )
            this.twitter.postValue("")
    }

    fun onWhatsappClicked(){
        openWhatsapp.postValue(true)
    }

    fun onFacebookClicked(){
        openFacebook.postValue(true)
    }

    fun onInstagramClicked(){
        openInstagram.postValue(true)
    }

    fun onLinkedinCliked(){
        openLinkedin.postValue(true)
    }

    fun onTwitterCliked(){
        openTwitter.postValue(true)
    }
}