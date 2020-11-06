package br.com.noclaftech.showin.presentation.scheduleshow

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityScheduleShowStep1Binding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.DateDialog
import br.com.noclaftech.showin.presentation.HourDialog
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.showin.presentation.util.ImageHelper
import com.afollestad.materialdialogs.MaterialDialog
import java.util.*
import javax.inject.Inject

class ScheduleShowStep1Activity : BaseActivity() {
    private lateinit var imageHelper : ImageHelper

    @Inject
    lateinit var viewModel: ScheduleShowViewModel

    lateinit var binding : ActivityScheduleShowStep1Binding
    private var alert: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_show_step1)

        screenComponent.inject(this)
        imageHelper = ImageHelper(this)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            setupError(it.name, viewModel.getNameError())
            setupError(it.date, viewModel.getDateError())
            setupError(it.hour, viewModel.getHourError())
            setupError(it.duration, viewModel.getDurationError())
            setupError(it.description, viewModel.getDescriptionError())
            setupError(it.recordPosition, viewModel.getRecordPositionError())
            setupError(it.ageRating, viewModel.getAgeRatingError())
            setupError(it.category, viewModel.getCategoryError())
        }

        viewModel.name.observe(this, Observer {
            if (it.length == 40){
                binding.name.error = getString(R.string.max_length)
            }
        })

        viewModel.description.observe(this, Observer {
            if (it.length == 300){
                binding.description.error = getString(R.string.max_length)
            }
        })

        viewModel.getDurationError().observe(this,
            Observer {
                it?.let {
                   if (!it)
                       binding.duration.error = null
                }
            })

        viewModel.getAgeRatingError().observe(this,
            Observer {
                it?.let {
                    if (!it)
                        binding.ageRating.error = null
                }
            })

        viewModel.getInvalidHour().observe(this,
            Observer {
                it?.let { bool ->
                    if(bool) binding.hour.error = getString(R.string.invalid_hour)
                }
            })

        viewModel.getInvalidDate().observe(this,
            Observer {
                it?.let { bool ->
                    if(bool) binding.date.error = getString(R.string.invalid_date)
                }
            })

        viewModel.getOpenCamera().observe(this,
            Observer {
                it?.let {
                    if (it)
                        imageHelper.showDialogGalleryOrCamera()
                }
            })

        viewModel.getOpenAgeRating().observe(this,
            Observer {
                it?.let {
                    if (it)
                        alert = MaterialDialog.Builder(this)
                            .title(getString(R.string.age_rating))
                            .items(Helper.getAgeRating())
                            .itemsCallback { _, _, _, text ->
                                viewModel.setAgeRating(text.toString())
                            }
                            .show()
                }
            })

        viewModel.getOpenRecordPosition().observe(this,
        Observer {
            it.let{
                if (it)
                    alert = MaterialDialog.Builder(this)
                        .title(getString(R.string.record_position))
                        .items(Helper.getRecordPosition(this))
                        .itemsCallback { _, _, _, text ->
                            viewModel.setRecordPosition(text.toString())
                            binding.recordPosition.setText(text.toString())
                        }
                        .show()
            }
        })

        viewModel.getOpenCategory().observe(this,
            Observer {
                it?.let {
                    if (it)
                        alert = MaterialDialog.Builder(this)
                            .title(getString(R.string.category))
                            .items(Helper.getCategories())
                            .itemsCallback { _, _, _, text ->
                                viewModel.setCategory(text.toString())
                            }
                            .show()
                }
            })

        viewModel.let {
            it.getRecordPositionError().observe(this,
                Observer { bool ->
                    run {
                        if (!bool)
                            binding.recordPosition.error = null
                    }
                })
        }

        viewModel.getOpenDuration().observe(this,
            Observer {
                it?.let {
                    if (it)
                        alert = MaterialDialog.Builder(this)
                            .title(getString(R.string.show_duration))
                            .items(Helper.getDuration())
                            .itemsCallback { _, _, _, text ->
                                viewModel.setDuration(text.toString())
                            }
                            .show()
                }
            })

        viewModel.getNotImageHorizontal().observe(this,
            Observer {
                it?.let {
                    if(it)
                        setAlert(getString(R.string.insert_horizontal_image))
                }
            })

        viewModel.getNotImageVertical().observe(this,
            Observer {
                it?.let {
                    if(it)
                        setAlert(getString(R.string.insert_vertical_image))
                }
            })

        viewModel.getOpenDate().observe(this,
            Observer{
                it?.let{
                    if(it){
                        openDate()
                    }
                }
            })

        viewModel.getOpenHour().observe(this,
        Observer {
            it?.let{
                if(it){
                    openHour()
                }
            }
        })}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ImageHelper.GALLERY_REQUEST_CODE ||
            requestCode == ImageHelper.PHOTO_REQUEST_CODE)
            imageHelper.handleResult(requestCode, resultCode, data, object : ImageHelper.Callback{
                override fun onImageCompressed(image64: String?, imageBitmap: Bitmap?) {

                    if (viewModel.getHorizontal().value!!)
                        binding.horizontalImage.setImageBitmap(imageBitmap)
                    else
                        binding.verticalImage.setImageBitmap(imageBitmap)
                    viewModel.setImageThumb(image64)

                }

                override fun onCanceled() {
                    Log.e("EditProfileActivity", "Image Canceled")
                }

                override fun onError() {
                    //toast(R.string.image_error)
                }
            })

    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imageHelper.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun setAlert(text : String){
        AlertHelper.alertGenericTwoButtons(this,
            getString(R.string.error),
            text,
            getString(R.string.ok),
            Color.parseColor("#4089e7"),
            DialogInterface.OnClickListener { _, _ ->  },
            null, null)
    }

    private fun openHour(){
        HourDialog.getInstance(getString(R.string.live_hour), getString(R.string.live_date_subtitle)){
            viewModel.updateHour(it)
        }.show(supportFragmentManager, "")
    }

    private fun openDate(){
        DateDialog
            .getInstance(getString(R.string.live_date), getString(R.string.live_date_subtitle)) {
//            .getInstance("", "") {
            viewModel.updateDate(it)
        }.show(supportFragmentManager, "")

//        val myCalendar = Calendar.getInstance()
//        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
//            myCalendar.set(Calendar.YEAR, year)
//            myCalendar.set(Calendar.MONTH, monthOfYear)
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            viewModel.updateDate(myCalendar)
//        }
//
//        //Theme_Holo_Light_Dialog_MinWidth
//        val dataPicker = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, date, myCalendar.get(
//            Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//            myCalendar.get(Calendar.DAY_OF_MONTH))
//        dataPicker.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dataPicker.show()
    }
}
