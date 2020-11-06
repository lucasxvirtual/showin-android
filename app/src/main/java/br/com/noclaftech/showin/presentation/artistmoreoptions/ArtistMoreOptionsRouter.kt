package br.com.noclaftech.showin.presentation.artistmoreoptions

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.tutorials.TutorialsActivity
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.about.AboutActivity
import br.com.noclaftech.showin.presentation.addsocials.AddSocialsActivity
import br.com.noclaftech.showin.presentation.artistinfo.ArtistInfoActivity
import br.com.noclaftech.showin.presentation.changepassword.ChangePasswordActivity
import br.com.noclaftech.showin.presentation.contact.ContactUsActivity
import br.com.noclaftech.showin.presentation.login.LoginActivity
import br.com.noclaftech.showin.presentation.paymentinfo.PaymentInfoActivity
import br.com.noclaftech.showin.presentation.registrationinformation.RegistrationInformationActivity
import br.com.noclaftech.showin.presentation.settings.SettingsActivity
import java.lang.ref.WeakReference

class ArtistMoreOptionsRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        LOGIN,
        REGISTRATION_INFORMATION,
        ADD_SOCIALS,
        ARTIST_INFORMATION,
        CONTACT_US,
        ABOUT,
        TUTORIALS,
        SETTINGS,
        CHANGE_PASSWORD,
        PAYMENT_INFO
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.LOGIN -> { showNextScreenClearTask(LoginActivity::class.java, bundle) }
            Route.REGISTRATION_INFORMATION -> { showNextScreen(RegistrationInformationActivity::class.java, bundle) }
            Route.ADD_SOCIALS -> {showNextScreen(AddSocialsActivity::class.java, bundle)}
            Route.ARTIST_INFORMATION -> { showNextScreen(ArtistInfoActivity::class.java, bundle) }
            Route.CHANGE_PASSWORD -> {showNextScreen(ChangePasswordActivity::class.java, bundle)}
            Route.TUTORIALS -> {showNextScreen(TutorialsActivity::class.java, bundle)}
            Route.SETTINGS -> { showNextScreen(SettingsActivity::class.java, bundle) }
            Route.CONTACT_US -> { showNextScreen(ContactUsActivity::class.java, bundle) }
            Route.ABOUT -> { showNextScreen(AboutActivity::class.java, bundle) }
            Route.PAYMENT_INFO -> { showNextScreen(PaymentInfoActivity::class.java, bundle) }
        }
    }
}