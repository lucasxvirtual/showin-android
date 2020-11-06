package br.com.noclaftech.showin.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import java.lang.ref.WeakReference

open class BaseRouter(private val activityRef: WeakReference<Activity>){
    open fun goBack(){
        activityRef.get()?.onBackPressed()
    }

    internal open fun showNextScreen(clazz: Class<*>, bundle: Bundle?) {
        activityRef.get()?.startActivity(Intent(activityRef.get(), clazz).putExtras(bundle!!))
    }

    fun startForResult(clazz: Class<*>, bundle: Bundle?, requestCode: Int){
        activityRef.get()?.startActivityForResult(Intent(activityRef.get(), clazz).putExtras(bundle!!), requestCode)
    }

    fun resultFinish(bundle: Bundle, resultOk: Boolean=true) {
        val result = if (resultOk) Activity.RESULT_OK else Activity.RESULT_CANCELED
        val data = Intent()
        data.putExtras(bundle)
        activityRef.get()?.setResult(result, data)
        activityRef.get()?.finish()
    }

    internal open fun showNextScreenClearTask(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(activityRef.get(), clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME)
        intent.putExtras(bundle!!)
        activityRef.get()?.startActivity(intent)
    }

    internal open fun goToWebPage(link: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        activityRef.get()?.startActivity(intent)
    }
}