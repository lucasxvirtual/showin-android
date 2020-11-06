package br.com.noclaftech.showin.presentation.profile

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.addsocials.AddSocialsActivity
import br.com.noclaftech.showin.presentation.followers.FollowersActivity
import br.com.noclaftech.showin.presentation.mymessages.MyMessagesActivity
import br.com.noclaftech.showin.presentation.mysocials.MySocialsActivity
import br.com.noclaftech.showin.presentation.profilemoreoptions.ProfileMoreOptionsActivity
import br.com.noclaftech.showin.presentation.usernotification.UserNotificationActivity
import java.lang.ref.WeakReference

class ProfileRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        NOTIFICATIONS,
        MORE_OPTIONS,
        FOLLOWERS,
        MY_SOCIALS,
        MY_MESSAGES,
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.NOTIFICATIONS -> {showNextScreen(UserNotificationActivity::class.java, bundle)}
            Route.MORE_OPTIONS -> {showNextScreen(ProfileMoreOptionsActivity::class.java, bundle)}
            Route.FOLLOWERS -> {showNextScreen(FollowersActivity::class.java, bundle)}
            Route.MY_SOCIALS -> {showNextScreen(AddSocialsActivity::class.java, bundle)}
            Route.MY_MESSAGES -> { showNextScreen(MyMessagesActivity::class.java, bundle) }
        }
    }
}