package br.com.noclaftech.showin.presentation.showdetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityShowDetailsBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.TestMomentDialog
import br.com.noclaftech.showin.presentation.streaming.StreamingActivity
import br.com.noclaftech.showin.presentation.util.AlertHelper
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.floor


class ShowDetailsActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: ShowDetailsViewModel

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityShowDetailsBinding = DataBindingUtil.setContentView(this, layout.activity_show_details)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel

            it.buy.setOnClickListener {
                val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                viewModel.onBuyClicked(deviceId)
            }
        }

        viewModel.getUser().observe(this,
            Observer {
                it?.let {
                    binding.date.text = viewModel.getDate()
                    binding.hour.text = viewModel.getHour()
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

        viewModel.getIsHimSelf().observe(this,
            Observer {
                if (it) {
                    binding.edit.visibility = View.VISIBLE
                    binding.ticketInfo.visibility = View.VISIBLE
                }
        })

        viewModel.buttonName.observe(this, Observer {
            it?.let {
                binding.buy.text = when(it){
                    1 -> getString(R.string.start)
                    2 -> getString(R.string.watch)
                    3 -> daysDiff(viewModel.show.value!!.date)
                    else -> getString(R.string.buy)
                }
                binding.buy.background = when(it){
                    3 -> resources.getDrawable(R.drawable.bg_border_search, resources.newTheme())
                    else -> resources.getDrawable(R.drawable.bg_multicolor_button, resources.newTheme())
                }
            }
        })

        viewModel.getAlert().observe(this,
            Observer {
            it?.let {
                setAlert(it)
            }
        })

        viewModel.getDeepLink().observe(this,
            Observer {
                it?.let {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, it)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            })

        processDynamicLink()
    }

    override fun getMessage(number: Int?): String? {
        return when(number) {
            1 -> getString(R.string.cant_config)
            else -> null
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.bound(intent.getIntExtra(EXTRA_SHOW, -1))
    }

    private fun setAlert(text : String){
        requestPermissions()
//        AlertHelper.alertGenericTwoButtons(this,
//            getString(R.string.stream),
//            text,
//            getString(R.string.device_stream),
//            Color.parseColor("#4089e7"),
//            DialogInterface.OnClickListener { _, _ -> requestPermissions() },
//            getString(R.string.another_tool), DialogInterface.OnClickListener { _, _ -> viewModel.onClickAnotherTool() })
    }

    private fun requestPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                StreamingActivity.REQUEST_CODE
            )
        } else {
            TestMomentDialog{
                viewModel.onClickStream()
            }.show(supportFragmentManager, "")
        }
    }

    private fun processDynamicLink(){
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                if (pendingDynamicLinkData != null) {
                    viewModel.bound(pendingDynamicLinkData.link?.lastPathSegment!!.toInt())
                }

            }
            .addOnFailureListener(
                this
            ) { Log.e("DYNAMIC LINK", it.message!!) }
    }

    override fun getBaseViewModel() = viewModel

    companion object {
        const val EXTRA_SHOW = "show_id"
    }

    override fun onBackPressed() {
        viewModel.onBackClicked()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == StreamingActivity.REQUEST_CODE){
            for(result in grantResults){
                if (result != PackageManager.PERMISSION_GRANTED){
                    requestPermissions()
                    return
                }
            }
            TestMomentDialog{
                viewModel.onClickStream()
            }.show(supportFragmentManager, "")

        }
    }

    private fun daysDiff(showDate: String) : String{
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val currentDate = formatter.format(Date())

        val currentDateFormat = formatter.parse(currentDate)
        val showDateFormat = formatter.parse(showDate)

        val diffSeconds = (showDateFormat!! .time - currentDateFormat!!.time) / 1000

        return getDate((diffSeconds.toFloat()))
    }

    fun getDate(seconds: Float): String {

        val d = floor(seconds / 86400)
        val h = floor((seconds / 3600) - (d * 24))
        val m = floor(seconds % 3600 / 60)

        return String.format("%.0fd %.0fh %.0fm", d, h, m)
    }
}
