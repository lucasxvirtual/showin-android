package br.com.noclaftech.showin.presentation.util

import android.content.Context
import androidx.core.os.bundleOf
import com.facebook.appevents.AppEventsLogger

class FacebookLog {

    companion object {

        fun logSearch(context: Context){
            AppEventsLogger.newLogger(context).apply {
                logEvent("Search")
            }
        }

        fun logActivity(context: Context){
            AppEventsLogger.newLogger(context).apply {
                logEvent("PageView")
            }
        }

        fun logContact(context: Context){
            AppEventsLogger.newLogger(context).apply {
                logEvent("Contact")
            }
        }

        fun logPrePack(context: Context){
            AppEventsLogger.newLogger(context).apply {
                logEvent("InitiateCheckout")
            }
        }

        fun logPosPack(context: Context, value: Float, currency : String = "BRL"){
            AppEventsLogger.newLogger(context).apply {
                logEvent("Purchase", bundleOf("value" to value, "currency" to currency))
            }
        }

    }

}