package br.com.noclaftech.showin.presentation.about

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import br.com.noclaftech.showin.BuildConfig
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityAboutBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import javax.inject.Inject

class AboutActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: AboutViewModel

    override fun getBaseViewModel() = viewModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAboutBinding = DataBindingUtil.setContentView(this, layout.activity_about)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel


            it.webView.apply {
                settings.javaScriptEnabled = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                settings.userAgentString = getString(R.string.app_name)
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()
                loadUrl("https://showin.tv/about-mobile")
            }
        }
    }
}