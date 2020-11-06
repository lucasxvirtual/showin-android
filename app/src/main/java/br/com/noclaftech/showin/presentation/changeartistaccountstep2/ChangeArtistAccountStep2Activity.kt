package br.com.noclaftech.showin.presentation.changeartistaccountstep2

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityChangeArtistAccountStep2Binding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.Helper
import javax.inject.Inject

class ChangeArtistAccountStep2Activity : BaseActivity() {

    @Inject
    lateinit var viewModel: ChangeArtistAccountStep2ViewModel
    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityChangeArtistAccountStep2Binding = DataBindingUtil.setContentView(this, layout.activity_change_artist_account_step2)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel

            setupError(it.bio, viewModel.getBioError())
        }

        viewModel.setChangeArtist(intent.getSerializableExtra("changeArtist")!!)

        viewModel.bio.observe(this, Observer {
            if (it.length == 150){
                binding.bio.error = getString(R.string.max_length)
            }
        })

        viewModel.let {

            it.getOpenTermsUse().observe(this,
                Observer {bool ->
                        if (bool)
                            Helper.openTermsUse(this)
                })

            it.getNotAccept().observe(this,
                Observer {bool->
                    if (bool) setAlert(getString(R.string.you_need_accept_terms))
                })
        }
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