package br.com.noclaftech.showin.presentation.editshow

import android.app.Activity
import android.os.Bundle
import br.com.noclaftech.showin.presentation.BaseRouter
import java.lang.ref.WeakReference

class EditShowRouter(private val activityRef: WeakReference<Activity>) : BaseRouter(activityRef) {

    enum class Route {

    }

    fun navigate (route: Route, bundle: Bundle = Bundle()){
        when(route){

        }
    }
}