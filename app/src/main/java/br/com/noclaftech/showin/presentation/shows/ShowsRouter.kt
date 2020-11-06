package br.com.noclaftech.showin.presentation.shows

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.scheduleshow.ScheduleShowStep1Activity
import br.com.noclaftech.showin.presentation.scheduleshow.ScheduleShowStep2Activity
import br.com.noclaftech.showin.presentation.showdetails.ShowDetailsActivity
import br.com.noclaftech.showin.presentation.streaming.OtherToolActivity
import br.com.noclaftech.showin.presentation.streaming.StreamingActivity
import java.lang.ref.WeakReference

class ShowsRouter (private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        SCHEDULE_SHOW,
        SHOW_DETAILS,
        STREAM,
        OTHER_TOOL
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.SCHEDULE_SHOW -> { showNextScreen(ScheduleShowStep1Activity::class.java, bundle) }
            Route.SHOW_DETAILS -> { showNextScreen(ShowDetailsActivity::class.java, bundle) }
            Route.STREAM -> { showNextScreen(StreamingActivity::class.java, bundle) }
            Route.OTHER_TOOL -> { showNextScreen(OtherToolActivity::class.java, bundle) }
        }
    }
}