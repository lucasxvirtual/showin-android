package br.com.noclaftech.showin.presentation.streaming

import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityStreamingBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.EndStreamDialog
import br.com.noclaftech.showin.presentation.adapter.ChatAdapter
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.Helper
import com.github.faucamp.simplertmp.RtmpHandler
import com.github.faucamp.simplertmp.RtmpHandler.RtmpListener
import net.ossrs.yasea.SrsEncodeHandler
import net.ossrs.yasea.SrsEncodeHandler.SrsEncodeListener
import net.ossrs.yasea.SrsPublisher
import net.ossrs.yasea.SrsRecordHandler
import net.ossrs.yasea.SrsRecordHandler.SrsRecordListener
import java.io.IOException
import java.net.SocketException
import javax.inject.Inject

class StreamingActivity : BaseActivity(), RtmpListener, SrsRecordListener, SrsEncodeListener {

    lateinit var binding: ActivityStreamingBinding
    @Inject
    lateinit var viewModel: StreamingViewModel
    private lateinit var mPublisher: SrsPublisher
    private var publishing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_streaming)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        screenComponent.inject(this)

        val adapter = ChatAdapter(emptyList()).apply { onItemClickedListener = {viewModel.onItemClicked() } }

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            it.chat.layoutManager = LinearLayoutManager(this)
            it.chat.adapter = adapter

            it.switchCamera.setOnClickListener {
                switchCamera()
            }
        }

        viewModel.live.observe(this, Observer {
            it.let {
                if (!it){
                    binding.buttonLinearLayout.visibility = View.GONE
                    binding.buttonLinearLayout2.visibility = View.VISIBLE
                    binding.viewersCountLinear.visibility = View.GONE
                    binding.chat.visibility = View.GONE
                    binding.emojisScrollView.visibility = View.GONE
                    binding.typeMessageConstraintLayout.visibility = View.GONE
                } else {
                    viewModel.startTimer()
                    binding.buttonLinearLayout.visibility = View.VISIBLE
                    binding.buttonLinearLayout2.visibility = View.GONE
                    binding.viewersCountLinear.visibility = View.VISIBLE
                    binding.chat.visibility = View.VISIBLE
                    binding.emojisScrollView.visibility = View.VISIBLE
                    binding.typeMessageConstraintLayout.visibility = View.VISIBLE
                }
            }
        })

        viewModel.show.observe(this, Observer {
            it?.let{
                if(it.position != "VERTICAL"){
                    binding.buttonLinearLayout.visibility = View.GONE
                    binding.buttonLinearLayout2.visibility = View.VISIBLE
                    Helper.setMargins(binding.chat, 0, 0, 300, 0)
                } else {
                    binding.buttonLinearLayout.visibility = View.VISIBLE
                    binding.buttonLinearLayout2.visibility = View.GONE
                }
            }
        })

        viewModel.getNoChat().observe(this, Observer {
            if (it){
                binding.chatIcon.background = resources.getDrawable(R.drawable.bg_white, resources.newTheme())
                binding.chatIcon.setColorFilter(resources.getColor(R.color.black, resources.newTheme()))
                binding.chat.visibility = View.GONE
                binding.emojisScrollView.visibility = View.GONE
                binding.typeMessageConstraintLayout.visibility = View.GONE
            } else {
                binding.chatIcon.background = resources.getDrawable(R.drawable.bg_white_border, resources.newTheme())
                binding.chatIcon.setColorFilter(resources.getColor(R.color.white, resources.newTheme()))
                binding.chat.visibility = View.VISIBLE
                binding.emojisScrollView.visibility = View.VISIBLE
                binding.typeMessageConstraintLayout.visibility = View.VISIBLE
            }
        })

        viewModel.chatMessages.observe(this, Observer {
            val imm: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.message.windowToken, 0)
        })

        viewModel.liveConfig.observe(this, Observer {
            it?.let {
                publishing = true
                mPublisher.startPublish(it.link)
                mPublisher.startCamera()
            }
        })

        viewModel.getAlert().observe(this,
            Observer {
                if(it)
                    setAlert()
            })
    }

    private fun setAlert(){
        EndStreamDialog {
            viewModel.finishLive()
        }.show(supportFragmentManager, "")
    }

    private fun switchCamera(){
        mPublisher.switchCameraFace((mPublisher.cameraId + 1) % 2)
    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        mPublisher.stopPublish()
        mPublisher.stopRecord()
        mPublisher.stopCamera()
        mPublisher.stopAudio()
        mPublisher.stopEncode()
    }

    override fun onResume() {
        super.onResume()
        val show = intent.extras!![EXTRA_SHOW] as Show

        if (show.position == "VERTICAL"){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            init(480, 854)
            switchCamera()
            switchCamera()
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            init(854, 480)
            switchCamera()
        }
        viewModel.bound(show)
    }

    override fun onPause() {
        super.onPause()
        mPublisher.stopPublish()
        mPublisher.stopRecord()
        mPublisher.stopCamera()
        mPublisher.stopAudio()
        mPublisher.stopEncode()
    }

    override fun onStop() {
        super.onStop()
        viewModel.unbound()
        mPublisher.stopPublish()
        mPublisher.stopRecord()
        mPublisher.stopCamera()
        mPublisher.stopAudio()
        mPublisher.stopEncode()
    }

    private fun init(width: Int, height: Int){
        val mCameraView = binding.glsurfaceviewCamera
        mPublisher = SrsPublisher(mCameraView)
        mPublisher.setEncodeHandler(SrsEncodeHandler(this))
        mPublisher.setRtmpHandler(RtmpHandler(this))
        mPublisher.setRecordHandler(SrsRecordHandler(this))
        mPublisher.setPreviewResolution(854, 480)
        mPublisher.setOutputResolution(width, height)

        mPublisher.setVideoHDMode()
        mPublisher.startCamera()
        mPublisher.switchToSoftEncoder()
    }

    companion object {
        const val EXTRA_SHOW = "show"
        const val REQUEST_CODE = 650
    }

    private fun handleException(e: Exception) {
        try {
            Toast.makeText(
                applicationContext,
                getString(R.string.connection_error),
                Toast.LENGTH_SHORT
            ).show()
            mPublisher.stopPublish()
        } catch (e1: Exception) { //
        }
    }

    // Implementation of SrsRtmpListener.

    // Implementation of SrsRtmpListener.
    override fun onRtmpConnecting(msg: String?) {
        Toast.makeText(
            applicationContext,
            getString(R.string.connecting),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRtmpConnected(msg: String?) {
        Toast.makeText(
            applicationContext,
            getString(R.string.connected),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRtmpVideoStreaming() {}

    override fun onRtmpAudioStreaming() {}

    override fun onRtmpStopped() {
    }

    override fun onRtmpDisconnected() {
        Toast.makeText(
            applicationContext,
            getString(R.string.disconnected),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRtmpVideoFpsChanged(fps: Double) {
        Log.i("onRtmpVideoFpsChanged", String.format("Output Fps: %f", fps))
    }

    override fun onRtmpVideoBitrateChanged(bitrate: Double) {
        val rate = bitrate.toInt()
        if (rate / 1000 > 0) {
            Log.i(
                "onRtmpVideoBitrateChang",
                String.format("Video bitrate: %f kbps", bitrate / 1000)
            )
        } else {
            Log.i(
                "onRtmpVideoBitrateChang",
                String.format("Video bitrate: %d bps", rate)
            )
        }
    }

    override fun onRtmpAudioBitrateChanged(bitrate: Double) {
        val rate = bitrate.toInt()
        if (rate / 1000 > 0) {
            Log.i(
                "onRtmpAudioBitrateChang",
                String.format("Audio bitrate: %f kbps", bitrate / 1000)
            )
        } else {
            Log.i(
                "onRtmpAudioBitrateChang",
                String.format("Audio bitrate: %d bps", rate)
            )
        }
    }

    override fun onRtmpSocketException(e: SocketException?) {
        handleException(e!!)
    }

    override fun onRtmpIOException(e: IOException?) {
        handleException(e!!)
    }

    override fun onRtmpIllegalArgumentException(e: IllegalArgumentException?) {
        handleException(e!!)
    }

    override fun onRtmpIllegalStateException(e: IllegalStateException?) {
        handleException(e!!)
    }

    // Implementation of SrsEncodeHandler.

    // Implementation of SrsEncodeHandler.
    override fun onNetworkWeak() {
        Toast.makeText(
            applicationContext,
            getString(R.string.weak),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNetworkResume() {
        Toast.makeText(
            applicationContext,
            getString(R.string.resume),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onEncodeIllegalArgumentException(e: java.lang.IllegalArgumentException?) {
        handleException(e!!)
    }

    // Implementation of SrsRecordHandler.
    override fun onRecordPause() {
    }

    override fun onRecordResume() {
    }

    override fun onRecordStarted(msg: String) {
    }

    override fun onRecordFinished(msg: String) {
    }

    override fun onRecordIOException(e: IOException?) {
        handleException(e!!)
    }

    override fun onRecordIllegalArgumentException(e: java.lang.IllegalArgumentException?) {
        handleException(e!!)
    }

    override fun getMessage(number: Int?): String? {
        return when(number){
            StreamingViewModel.ToastMessage.LIVE.value -> getString(R.string.live_error)
            StreamingViewModel.ToastMessage.FINISH.value -> getString(R.string.finish_error)
            else -> null
        }
    }

}
