package br.com.noclaftech.showin.presentation.util.socialnetworkutil

import com.facebook.login.LoginResult
import java.io.Serializable

data class FacebookResult(val accessToken: LoginResult?,
                          val error: Boolean = false,
                          val errorMessage: String? = null) : Serializable