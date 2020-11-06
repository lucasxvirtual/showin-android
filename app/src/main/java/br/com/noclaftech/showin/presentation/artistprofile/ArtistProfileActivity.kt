package br.com.noclaftech.showin.presentation.artistprofile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityArtistProfileBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import javax.inject.Inject
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.presentation.adapter.ArtistMessagesAdapter
import br.com.noclaftech.showin.presentation.adapter.ShowHorizontalAdapter
import br.com.noclaftech.showin.presentation.adapter.ShowVerticalAdapter
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.Helper

class ArtistProfileActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ArtistProfileViewModel

    override fun getBaseViewModel() = viewModel

    lateinit var binding : ActivityArtistProfileBinding

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layout.activity_artist_profile)

        screenComponent.inject(this)

        viewModel.bound(intent.getIntExtra(EXTRA_ARTIST, -1))

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            binding.recyclerNextShows.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerNextShows.isNestedScrollingEnabled = false


            binding.recyclerMessages.layoutManager = LinearLayoutManager(this)
            binding.recyclerMessages.isNestedScrollingEnabled = false

        }

        viewModel.let {
            it.getOpenWhatsapp().observe(this,
                Observer {
                    if (it)
                        Helper.whatsapp(this, viewModel.whatsapp.value!!)
                })

            it.getOpenFacebook().observe(this,
                Observer {
                    if (it)
                        Helper.facebook(this, viewModel.facebook.value!!)
                })

            it.getOpenInstagram().observe(this,
                Observer {
                    if (it)
                        Helper.instagram(this, viewModel.instagram.value!!)
                })

            it.getOpenLinkedin().observe(this,
                Observer {
                    if (it)
                        Helper.linkedin(this, viewModel.linkedin.value!!)
                })

            it.getOpenTwitter().observe(this,
                Observer {
                    if (it)
                        Helper.twitter(this, viewModel.twitter.value!!)
                })
        }

        viewModel.getUserOb().observe(this,
        Observer {
            it?.let {
                binding.nameArtist.text = it.artist?.artisticName
                binding.userName.text = "${Helper.getAt(it.username)}"

                if (it.artist?.verified == true)
                    binding.verifiedIcon.visibility = View.VISIBLE
                else
                    binding.verifiedIcon.visibility = View.INVISIBLE

            }
        })

        viewModel.getShowsFuture().observe(this,
            Observer {
                it?.let {

                    val adapter = ShowHorizontalAdapter(it)
                    adapter.onItemClickedListener = { viewModel.onClickItem(it) }
                    adapter.onButtonClickedListener = {
                        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                        viewModel.onButtonClicked(it, deviceId)
                    }

                    binding.recyclerNextShows.adapter = adapter
                }
            }
        )

        viewModel.getMessages().observe(this,
            Observer {
                it?.let {
                    val adapter = ArtistMessagesAdapter(it)
                    binding.recyclerMessages.adapter = adapter
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    companion object {
        const val EXTRA_ARTIST = ""
    }
}