package br.com.noclaftech.showin.presentation.home

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import br.com.noclaftech.showin.presentation.buyticket.BuyTicketActivity
import br.com.noclaftech.showin.presentation.changeartistaccountstep1.ChangeArtistAccountStep1Activity
import br.com.noclaftech.showin.presentation.main.MainRouter
import br.com.noclaftech.showin.presentation.scheduleshow.ScheduleShowStep1Activity
import br.com.noclaftech.showin.presentation.search.SearchActivity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.showin.presentation.streaming.StreamingActivity
import br.com.noclaftech.showin.presentation.watch.WatchActivity
import java.lang.ref.WeakReference

class HomeRouter (activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        SHOW_DETAILS,
        BUY,
        WATCH,
        ARTIST,
        SEARCH,
        WEB,
        SCHEDULE,
        CHANGE_TO_ARTIST_ACCOUNT
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.SHOW_DETAILS -> { showNextScreen(ShowDetailsActivity::class.java, bundle) }
            Route.BUY -> { showNextScreen(BuyTicketActivity::class.java, bundle) }
            Route.WATCH -> { showNextScreen(WatchActivity::class.java, bundle) }
            Route.ARTIST -> { showNextScreen(ArtistProfileActivity::class.java, bundle) }
            Route.SEARCH -> { showNextScreen(SearchActivity::class.java, bundle) }
            Route.WEB -> { goToWebPage(bundle["link"] as String) }
            Route.SCHEDULE -> { showNextScreen(ScheduleShowStep1Activity::class.java, bundle) }
            Route.CHANGE_TO_ARTIST_ACCOUNT -> { showNextScreen(ChangeArtistAccountStep1Activity::class.java, bundle) }
        }
    }
}