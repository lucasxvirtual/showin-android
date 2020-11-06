package br.com.noclaftech.showin.presentation.util.socialnetworkutil

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import org.json.JSONObject


class FacebookUtil {

    companion object {

        private var callbackManager: CallbackManager? = null
        private val loginResult = MutableLiveData<FacebookResult?>().apply { value = null }

        fun facebookInit(application: Application) {
            AppEventsLogger.activateApp(application)
        }

        fun clearLoginResult(){
            loginResult.value = null
        }

        fun login(activity: Activity): LiveData<FacebookResult?> {
            LoginManager.getInstance().logOut()
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(
                activity,
                listOf("public_profile", "email")
            )

            LoginManager.getInstance().registerCallback(callbackManager!!,
                object : FacebookCallback<com.facebook.login.LoginResult> {
                    override fun onSuccess(result: com.facebook.login.LoginResult?) {
                        if (result != null && result.accessToken.token != null)
                            loginResult.postValue(FacebookResult(result))
                    }

                    override fun onCancel() {
                    }

                    override fun onError(error: FacebookException?) {
                        loginResult.postValue(FacebookResult(null, true, error?.message))
                    }
                })

            return loginResult
        }

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }

        fun graphRequest(callback: (Any) -> Unit) {
            try {
                var json : JSONObject? = null
                val request = GraphRequest.newMeRequest(
                    loginResult.value!!.accessToken!!.accessToken,
                    GraphRequest.GraphJSONObjectCallback { `object`, response ->

                        response.jsonObject["picture"]

                        callback(response.jsonObject)


                    })
                val parameters = Bundle()
                parameters.putString("fields", "id,name,link, email, picture.width(500).height(500)")
                request.parameters = parameters
                request.executeAsync()


            }
            catch (e : FacebookException){

            }
        }
    }
}