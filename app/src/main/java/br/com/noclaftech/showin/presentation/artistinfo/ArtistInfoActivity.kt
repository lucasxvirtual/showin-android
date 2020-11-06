package br.com.noclaftech.showin.presentation.artistinfo

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityArtistInfoBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.AlertHelper
import javax.inject.Inject

class ArtistInfoActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ArtistInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityArtistInfoBinding = DataBindingUtil.setContentView(this,R.layout.activity_artist_info)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let{
            it.viewModel = viewModel
            setupError(it.artisticName, viewModel.getArtisticNameError())
            setupError(it.bio, viewModel.getBiographyError())
        }

        viewModel.let {

            it.getDetail().observe(this,
                Observer {
                    it?.let { string ->
                        setAlert(string)
                    }
                })
        }

        viewModel.getSuccess().observe(this,
            Observer {
                it?.let {
                    if (it){
                        Toast.makeText(this, getString(R.string.success_changes), Toast.LENGTH_LONG).show()
                        onBackPressed()
                    }
                }
            })
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

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }
}
