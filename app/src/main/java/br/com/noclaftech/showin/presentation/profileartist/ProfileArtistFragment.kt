package br.com.noclaftech.showin.presentation.profileartist

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ProfileArtistFragmentBinding
import br.com.noclaftech.showin.presentation.BaseFragment
import br.com.noclaftech.showin.presentation.adapter.ArtistMessagesAdapter
import br.com.noclaftech.showin.presentation.adapter.MyShowHorizontalAdapter
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.showin.presentation.util.ImageHelper
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import javax.inject.Inject

class ProfileArtistFragment : BaseFragment() {
    private lateinit var imageHelper : ImageHelper

    companion object {
        fun newInstance() = ProfileArtistFragment()
    }

    @Inject
    lateinit var viewModel: ProfileArtistViewModel

    lateinit var binding : ProfileArtistFragmentBinding

    override fun getBaseViewModel() = viewModel

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding =  ProfileArtistFragmentBinding.inflate(inflater, container, false)
        screenComponent.inject(this)

        imageHelper = ImageHelper(activity!!)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            viewModel.bound()


            binding.recyclerMessages.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            binding.recyclerMessages.isNestedScrollingEnabled = true
//            setupError(it.message, viewModel.getMessageError())

            binding.recyclerNextShows.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerNextShows.isNestedScrollingEnabled = false

        }

        viewModel.let {
            it.getOpenWhatsapp().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.whatsapp(activity!!, viewModel.whatsapp.value!!)
                })

            it.getOpenFacebook().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.facebook(activity!!, viewModel.facebook.value!!)
                })

            it.getOpenInstagram().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.instagram(activity!!, viewModel.instagram.value!!)
                })

            it.getOpenLinkedin().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.linkedin(activity!!, viewModel.linkedin.value!!)
                })

            it.getOpenTwitter().observe(viewLifecycleOwner,
                Observer {
                    if (it)
                        Helper.twitter(activity!!, viewModel.twitter.value!!)
                })
        }

//        viewModel.getUserOb().observe(viewLifecycleOwner,
//            Observer
//            {
//                it?.let{
//    //                binding.followers.text = it.followersNumber.toString()
//    //                binding.following.text = it.followingNumber.toString()
//                }
//            })

        viewModel.whatsapp.observe(viewLifecycleOwner, Observer {
            if ( it.isNullOrBlank() ){
                binding.whatsappIcon.visibility = View.GONE
            } else {
                binding.whatsappIcon.visibility = View.VISIBLE
            }
        })

        viewModel.instagram.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrBlank()){
                binding.instagramIcon.visibility = View.GONE
            } else {
                binding.instagramIcon.visibility = View.VISIBLE
            }
        })

        viewModel.facebook.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrBlank()){
                binding.facebookIcon.visibility = View.GONE
            } else {
                binding.facebookIcon.visibility = View.VISIBLE
            }
        })

        viewModel.linkedin.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrBlank()){
                binding.linkedinIcon.visibility = View.GONE
            } else {
                binding.linkedinIcon.visibility = View.VISIBLE
            }
        })

        viewModel.twitter.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrBlank()){
                binding.twitterIcon.visibility = View.GONE
            } else {
                binding.twitterIcon.visibility = View.VISIBLE
            }
        })

        viewModel.getOpen().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    if (it)
                        imageHelper.showDialogGalleryOrCamera()
                }
            })

        viewModel.getShowsFuture().observe(viewLifecycleOwner,
            Observer {
                if (it != null){
                    it.let {
                        val adapter = MyShowHorizontalAdapter(it)
                        adapter.onItemClickedListener = { viewModel.onClickItem(it) }
                        adapter.onButtonClickedListener = {
                            val deviceId = Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
                            viewModel.onButtonClicked(it, deviceId)
                        }
                        binding.recyclerNextShows.adapter = adapter
                    }
                } else {
                    binding.noNextLivesPlaceholder.visibility = View.VISIBLE
                }
            }
        )

        viewModel.getMessages().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    val adapter = ArtistMessagesAdapter(it)
                    binding.recyclerMessages.adapter = adapter
                }
            })

//        viewModel.getShowsOld().observe(viewLifecycleOwner,
//            Observer {
//                it?.let {
//                    val adapter = MyShowAdapter(it)
//                    adapter.onItemClickedListener = { viewModel.onClickItem(it) }
//                    adapter.onButtonClickedListener = {}
//                    binding.recyclerPastShows.adapter = adapter
//                }
//            }
//        )

        viewModel.getDetail().observe(viewLifecycleOwner,
            Observer {
                it?.let {
                    AlertHelper.alertGenericTwoButtons(activity!!,
                        getString(R.string.error),
                        it,
                        getString(R.string.ok),
                        Color.parseColor("#4089e7"),
                        DialogInterface.OnClickListener { _, _ ->  },
                        null, null)
                }
            })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUser()
        viewModel.getArtist()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ImageHelper.GALLERY_REQUEST_CODE ||
            requestCode == ImageHelper.PHOTO_REQUEST_CODE){

             val uri = imageHelper.handleResult(requestCode, resultCode, data)

            if (uri != null)
                CropImage.activity(uri)
                    .setAllowRotation(false)
                    .setAllowFlipping(false)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setFixAspectRatio(true)
                    .start(activity!!)
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                val result : CropImage.ActivityResult = CropImage.getActivityResult(data)
                val uri = result.uri
                imageHelper.handleResultForConvertBase64(uri, object : ImageHelper.Callback{
                    override fun onImageCompressed(image64: String?, imageBitmap: Bitmap?) {
                        viewModel.setImageThumb(image64)
                        binding.image.setImageBitmap(imageBitmap)
                    }

                    override fun onCanceled() {
                        Log.e("EditProfileActivity", "Image Canceled")
                    }

                    override fun onError() {
                        //toast(R.string.image_error)
                    }

                })
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //val error = result.error
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imageHelper.onRequestPermissionsResult(requestCode, grantResults)
    }
}