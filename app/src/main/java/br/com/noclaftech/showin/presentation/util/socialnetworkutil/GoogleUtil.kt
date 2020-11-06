package br.com.noclaftech.showin.presentation.util.socialnetworkutil

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.lang.Exception

class GoogleUtil(val activity: Activity, clientId: String) {

    private var mGoogleSignInClient : GoogleSignInClient


    companion object {
        const val RC_SIGN_IN = 10000
        val loginResult = MutableLiveData<GoogleResult?>().apply { value = null }

        fun clearGoogleResult(){
            loginResult.value = null
        }

    }

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(clientId)
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun login(): LiveData<GoogleResult?> {
        val signInIntent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
        return loginResult
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            //val account = completedTask.result
            //val idToken = account!!.idToken
            Log.e("AQUIIIIIIIIIIIIIIII", completedTask.result.toString())
            loginResult.postValue(GoogleResult(completedTask.result))
        } catch (e: Exception) {
            loginResult.postValue(GoogleResult(null, true, e.message))
            Log.w("GoogleUtil", "signInResult:failed code=" + e.message.toString())
        }
    }

    fun onActivityResult(requestCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
}