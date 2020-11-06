package br.com.noclaftech.showin.presentation.profilemoreoptions

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.about.AboutActivity
import br.com.noclaftech.showin.presentation.addsocials.AddSocialsActivity
import br.com.noclaftech.showin.presentation.changeartistaccountstep1.ChangeArtistAccountStep1Activity
import br.com.noclaftech.showin.presentation.changepassword.ChangePasswordActivity
import br.com.noclaftech.showin.presentation.contact.ContactUsActivity
import br.com.noclaftech.showin.presentation.login.LoginActivity
import br.com.noclaftech.showin.presentation.registrationinformation.RegistrationInformationActivity
import br.com.noclaftech.showin.presentation.settings.SettingsActivity
import java.lang.ref.WeakReference

class ProfileMoreOptionsRouter(private val activityRef: WeakReference<Activity>): BaseRouter(activityRef) {

    enum class Route {
        REGISTRATION_INFORMATION,
        ADD_SOCIALS,
        SWITCH_ACCOUNT,
        CHANGE_PASSWORD,
        SETTINGS,
        CONTACT_US,
        ABOUT,
        EXIT
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()){
        when(route) {
            Route.REGISTRATION_INFORMATION -> {showNextScreen(RegistrationInformationActivity::class.java, bundle)}
            Route.ADD_SOCIALS -> {showNextScreen(AddSocialsActivity::class.java, bundle)}
            Route.SWITCH_ACCOUNT -> { showNextScreen(ChangeArtistAccountStep1Activity::class.java, bundle) }
            Route.CHANGE_PASSWORD -> {showNextScreen(ChangePasswordActivity::class.java, bundle)}
            Route.SETTINGS -> {showNextScreen(SettingsActivity::class.java, bundle)}
            Route.CONTACT_US -> {showNextScreen(ContactUsActivity::class.java, bundle)}
            Route.ABOUT -> {showNextScreen(AboutActivity::class.java, bundle)}
            Route.EXIT -> {showNextScreen(LoginActivity::class.java, bundle)}
        }
    }
}