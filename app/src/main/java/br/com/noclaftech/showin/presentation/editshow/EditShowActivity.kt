package br.com.noclaftech.showin.presentation.editshow

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityEditShowBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.showin.presentation.util.ImageHelper
import com.afollestad.materialdialogs.MaterialDialog
import java.util.*
import javax.inject.Inject

class EditShowActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: EditShowViewModel
    override fun getBaseViewModel() = viewModel

    private var alert: MaterialDialog? = null
    private lateinit var imageHelper : ImageHelper

    private lateinit var binding : ActivityEditShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_show)

        screenComponent.inject(this)

        imageHelper = ImageHelper(this)

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            setupError(binding.name, viewModel.getNameError())
            setupError(binding.ageRating, viewModel.getAgeRatingError())
            setupError(binding.presentation, viewModel.getPresentationError())
            setupError(binding.recordPosition, viewModel.getRecordPositionError())
            setupError(binding.category, viewModel.getCategoryError())
        }

        viewModel.setShow(intent.getSerializableExtra("show")!!)

        viewModel.name.observe(this, Observer {
            if (it.length == 40){
                binding.name.error = getString(R.string.max_length)
            }
        })

        viewModel.presentation.observe(this, Observer {
            if (it.length == 300){
                binding.presentation.error = getString(R.string.max_length)
            }
        })

        viewModel.getOpenCamera().observe(this,
            Observer {
                it?.let {
                    if (it)
                        imageHelper.showDialogGalleryOrCamera()
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

        viewModel.let {
            it.getRecordPositionError().observe(this,
                Observer { bool ->
                    run {
                        if (!bool)
                            binding.recordPosition.error = null
                    }
                })
        }

        viewModel.getAgeRatingError().observe(this,
            Observer {
                it?.let {
                    if (!it)
                        binding.ageRating.error = null
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
    }


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
}