package br.com.noclaftech.showin.presentation.watch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.domain.model.ChatMessage
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityWatchBinding
import br.com.noclaftech.showin.ext.addFragment
import br.com.noclaftech.showin.ext.dp
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.ChatUserDialog
import br.com.noclaftech.showin.presentation.SupportStarDialog
import br.com.noclaftech.showin.presentation.donatecoins.DonateCoinsActivity
import br.com.noclaftech.showin.presentation.util.AlertHelper
import com.devbrackets.android.exomedia.core.video.scale.ScaleType
import com.devbrackets.android.exomedia.ui.widget.VideoView
import javax.inject.Inject


class WatchActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: WatchViewModel
    private lateinit var videoView: VideoView

    private lateinit var binding : ActivityWatchBinding

    private var position = "HORIZONTAL"

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_watch)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        screenComponent.inject(this)
        binding.viewModel = viewModel

        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        viewModel.bound(intent.getSerializableExtra(WATCH)!!, intent.getIntExtra(SHOWID, 0), deviceId)

        binding.let {
            it.lifecycleOwner = this

            videoView = it.videoview
        }

        viewModel.getFullScreen().observe(this, Observer {
            if (it)
                openFullscreenDialog()
            else
                closeFullscreenDialog()
        })

        viewModel.getShow().observe(this, Observer {
            it?.let {
                position = it.position!!
                if (position == "VERTICAL")
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        })

        viewModel.donate.observe(this, Observer {
            it?.let{
                if(it){
                    SupportStarDialog{ winnsAmount, password ->
                        viewModel.donate(winnsAmount, password)
                    }.show(supportFragmentManager, "")
                }
            }
        })

        viewModel.getDetail().observe(this,
            Observer {
                it?.let {
                    AlertHelper.alertGenericTwoButtons(this,
                        getString(R.string.error),
                        it,
                        getString(R.string.ok),
                        Color.parseColor("#4089e7"),
                        DialogInterface.OnClickListener { _, _ ->  },
                        null, null)
                }
            })

        viewModel.getAlert().observe(this,
            Observer {
                it?.let {
                    setAlert(viewModel.getPopUpUser().value!!,it)
                }
            })

        addFragment(WatchFragment.getInstance(viewModel))

    }

    private fun setAlert(user : User, message: ChatMessage){
        ChatUserDialog(user = user, message = message) { userId, message, reason ->
            viewModel.onReportClicked(userId, message, reason)
        }.show(supportFragmentManager, "")
    }

    private fun openFullscreenDialog() {
        binding.videoFrame.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        if(position == "HORIZONTAL" &&
            (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
                    requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        videoView.setScaleType(ScaleType.CENTER_CROP)
        addFragment(WatchFullScreenFragment.getInstance(viewModel))
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unbound()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            viewModel.setFullScreen(true)
        } else if(orientation == Configuration.ORIENTATION_PORTRAIT){
            viewModel.setFullScreen(false)
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun closeFullscreenDialog() {
        binding.videoFrame.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            200.dp()
        )
        if(requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        videoView.setScaleType(ScaleType.NONE)
        addFragment(WatchFragment.getInstance(viewModel))
    }

    override fun getBaseViewModel() = viewModel

    private fun setupVideoView() {
        binding.videoview.setOnPreparedListener { binding.videoview.start() }
        binding.videoview.setVideoURI(Uri.parse(viewModel.getWatch()))
    }

    override fun onPause() {
        super.onPause()
        binding.videoview.pause()
    }

    override fun onResume() {
        super.onResume()
        setupVideoView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 666 && resultCode == Activity.RESULT_OK) {
            viewModel.onDonated(data?.getIntExtra(DonateCoinsActivity.AMOUNT, -1))
        }
    }

    companion object {
        const val WATCH = "WATCH"
        const val SHOWID = "SHOWID"
        const val RESULT = 666
    }
}