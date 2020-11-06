package br.com.noclaftech.showin.presentation.util.socialnetworkutil

import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.io.Serializable

data class GoogleResult(val accessToken: GoogleSignInAccount?,
                          val error: Boolean = false,
                          val errorMessage: String? = null) : Serializable