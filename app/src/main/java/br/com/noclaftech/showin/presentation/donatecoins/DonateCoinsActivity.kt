package br.com.noclaftech.showin.presentation.donatecoins

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityDonateCoinsBinding
import br.com.noclaftech.showin.databinding.ActivityShowDetailsBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.AlertHelper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_donate_coins.*
import javax.inject.Inject

class DonateCoinsActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: DonateCoinsViewModel
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityDonateCoinsBinding = DataBindingUtil.setContentView(this, R.layout.activity_donate_coins)

        screenComponent.inject(this)

        binding.viewModel = viewModel
        viewModel.bound()

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
            setupError(it.value, viewModel.getValueError())
            setupError(it.receiver, viewModel.getReceiverError())
            setupError(it.password, viewModel.getPasswordError())
        }

        if(intent.hasExtra("show")) {
            viewModel.setShowId(intent.extras!!["show"] as Int)
            receiver.visibility = View.GONE
        }

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

    override fun getBaseViewModel() = viewModel

    override fun getMessage(number: Int?): String? {
        return when(number){
            1 -> getString(R.string.donation_success)
            else -> null
        }
    }

    companion object {
        const val AMOUNT = "amount"
    }
}