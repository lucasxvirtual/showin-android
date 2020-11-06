package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.content.Intent
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.BaseApplication
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.di.application.ApplicationComponent
import br.com.noclaftech.showin.di.screen.ScreenModule
import org.jetbrains.anko.toast

abstract class BaseFragment: Fragment() {

    companion object {
        const val REQUEST_CODE = "requestCode"
    }

    private var dialogTransparent : Dialog? = null

    val screenComponent by lazy {
        getApplicationComponent().plus(ScreenModule(activity as BaseActivity))
    }
    private fun getApplicationComponent(): ApplicationComponent {
        return (activity!!.application as BaseApplication).component
    }

    override fun onStart() {
        super.onStart()

        dialogTransparent = Dialog(activity!!, R.style.Theme_AppCompat_Light )
        val view = View.inflate(
            activity, R.layout.progress, null )
        dialogTransparent?.requestWindowFeature( Window.FEATURE_NO_TITLE )
        dialogTransparent?.window?.setBackgroundDrawableResource( R.color.transparent )
        dialogTransparent?.setContentView( view )

        getBaseViewModel()?.getLoadingVisibility()?.observe(this,
            Observer {
                it?.let { if(it) showDialog() else hideDialog() }
            })

        getBaseViewModel()?.getToast()?.observe(this,
            Observer {
                val message = getMessage(it)
                if(message != null)
                    activity?.toast(message)
            })
    }

    open fun getMessage(number: Int?): String?{
        return null
    }

    abstract fun getBaseViewModel(): BaseViewModel?

    /**
     * show the wait dialog
     */
    private fun showDialog( ){
        dialogTransparent?.show( )
    }

    /**
     * hide the wait dialog
     */
    private fun hideDialog( ){
        dialogTransparent?.dismiss( )
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        intent?.putExtra(REQUEST_CODE, requestCode)
        super.startActivityForResult(intent, requestCode)
    }
}