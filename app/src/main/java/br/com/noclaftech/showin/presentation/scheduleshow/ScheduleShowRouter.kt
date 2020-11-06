package br.com.noclaftech.showin.presentation.scheduleshow

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import br.com.noclaftech.showin.presentation.main.MainActivity
import java.lang.ref.WeakReference

class ScheduleShowRouter(activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {
        SCHEDULE_SHOW_2,
        SUCCESS,
        AGENDA,
        HOME,
        BACK
    }

    fun navigate(route: Route, bundle: Bundle = Bundle()) {
        when (route) {
            Route.SCHEDULE_SHOW_2 -> { showNextScreen(ScheduleShowStep2Activity::class.java, bundle) }
            Route.SUCCESS -> { showNextScreenClearTask(ScheduleShowStep3Activity::class.java, bundle) }
            Route.AGENDA -> { showNextScreenClearTask(MainActivity::class.java, bundle) }
            Route.HOME -> { showNextScreenClearTask(MainActivity::class.java, bundle) }
            Route.BACK -> { goBack() }
        }
    }

}