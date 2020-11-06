package br.com.noclaftech.showin.presentation.register

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
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityRegisterBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.DateDialog
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.showin.presentation.util.ImageHelper
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.FacebookUtil
import br.com.noclaftech.showin.presentation.util.socialnetworkutil.GoogleUtil
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class RegisterActivity : BaseActivity() {
    private lateinit var imageHelper : ImageHelper

    @Inject
    lateinit var viewModel: RegisterViewModel
    private val disposables = CompositeDisposable()
    private lateinit var binding : ActivityRegisterBinding
    private var alert: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout.activity_register)

        screenComponent.inject(this)

        imageHelper = ImageHelper(this)

        binding.viewModel = viewModel

        if(intent.hasExtra("loginFacebook")){
            viewModel.setResultFacebook(intent.getBooleanExtra("loginFacebook", false))
        }

        if(intent.hasExtra("loginGoogle")){
            viewModel.setResultGoogle(intent.getBooleanExtra("loginGoogle", false))
        }

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
            setupError(it.email, viewModel.getEmailError())
            setupError(it.userName, viewModel.getUsernameError())
            setupError(it.name, viewModel.getNameError())
            setupError(it.birthday, viewModel.getBirthdayError())
            setupError(it.gender, viewModel.getGenderError())
            setupError(it.password, viewModel.getPasswordError())
            setupError(it.confirmPassword, viewModel.getConfirmPasswordError())
        }

//        viewModel.getBitmap().observe(this,
//            Observer {
//                it?.let {
//                    viewModel.setImageThumb(imageHelper.getFormattedBase64(it))
//                    binding.image.setImageBitmap(it)
//                }
//            })

        viewModel.getSocialEmail().observe(this,
            Observer {
                it?.let {
                    binding.email.setText(it)
                }
            })

        viewModel.getSocialName().observe(this,
            Observer {
                it?.let {
                    binding.name.setText(it)
                }
            })

        viewModel.getOpenCamera().observe(this,
            Observer {
                it?.let {
                    if (it)
                        imageHelper.showDialogGalleryOrCamera()
                }
            })

        viewModel.getOpenGender().observe(this,
            Observer {
                it?.let {
                    if (it)
                        alert = MaterialDialog.Builder(this)
                            .title(getString(R.string.gender))
                            .items(Helper.getGender())
                            .itemsCallback { _, _, _, text ->
                                viewModel.setGender(text.toString())
                                binding.gender.setText(text.toString())
                            }
                            .show()
                }
            })

        viewModel.getOpenTermsUse().observe(this,
            Observer {
                it?.let {
                    if (it)
                        Helper.openTermsUse(this)
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

        viewModel.let {
            it.getGenderError().observe(this,
                Observer { bool ->
                    run {
                        if (!bool)
                            binding.gender.error = null
                    }
                })

            it.getIncorrectPasswordConfirmation().observe(this,
                Observer { bool ->
                        if(bool) binding.confirmPassword.error = getString(R.string.password_must_match)
                })

//            it.getNotImage().observe(this,
//                Observer {bool->
//                    if (bool) setAlert(getString(R.string.insert_photo))
//                })

            it.getNotAccept().observe(this,
                Observer {bool->
                    if (bool) setAlert(getString(R.string.you_need_accept_terms))
                })

            it.invalidDate().observe(this,
                Observer {bool->
                    if (bool) binding.birthday.error = getString(R.string.invalid_date)
                })

            it.getDetail().observe(this,
                Observer {
                    it?.let { string ->
                        setAlert(string)
                    }
                })
        }
    }

    override fun getBaseViewModel() = viewModel

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == ImageHelper.GALLERY_REQUEST_CODE ||
//            requestCode == ImageHelper.PHOTO_REQUEST_CODE)
//            imageHelper.handleResult(requestCode, resultCode, data, object : ImageHelper.Callback{
//                override fun onImageCompressed(image64: String?, imageBitmap: Bitmap?) {
//                    viewModel.setImageThumb(image64)
//                    binding.image.setImageBitmap(imageBitmap)
//                }
//
//                override fun onCanceled() {
//                    Log.e("EditProfileActivity", "Image Canceled")
//                }
//
//                override fun onError() {
//                    //toast(R.string.image_error)
//                }
//            })
//
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imageHelper.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun openDate(){
        DateDialog.getInstance(getString(R.string.birth_date), null) {
            viewModel.updateDate(it)
        }.show(supportFragmentManager, "")
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

    override fun onBackPressed() {
        FacebookUtil.clearLoginResult()
        GoogleUtil.clearGoogleResult()
        super.onBackPressed()
    }
}