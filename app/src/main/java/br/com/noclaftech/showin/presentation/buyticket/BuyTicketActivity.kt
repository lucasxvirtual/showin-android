package br.com.noclaftech.showin.presentation.buyticket

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityBuyTicketBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.AlertHelper
import javax.inject.Inject

class BuyTicketActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: BuyTicketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBuyTicketBinding = DataBindingUtil.setContentView(this, layout.activity_buy_ticket)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        if (intent.hasExtra(EXTRA_SHOW)){
            viewModel.setExtraShow(intent.getSerializableExtra(EXTRA_SHOW)!!)
        }
        else if (intent.hasExtra(EXTRA_SHOW_HOME)){
            viewModel.setExtraShowHome(intent.getSerializableExtra(EXTRA_SHOW_HOME)!!)
        }

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        viewModel.getInvalidBalance().observe(this,
            Observer {
                it?.let {
                    if (it)
                        alert(getString(R.string.insufficient_funds))
                }
            })

        viewModel.getInvalidQuantity().observe(this, Observer {
            it?.let {
                if(it){
                    alert(getString(R.string.invalid_quantity))
                }
            }
        })

        viewModel.getInvalidValue().observe(this,
            Observer {
                it?.let {
                    alert(String.format(getString(R.string.ticket_minimum_value), String.format("%.2f", it).replace(".", ",")))
                }
            })

        viewModel.getDetail().observe(this,
            Observer {
                it?.let {
                    alert(it)
                }
            })

        viewModel.numberOfTickets.observe(this, Observer {
            viewModel.updateValue()
        })
    }

    override fun getBaseViewModel() = viewModel

    companion object {
        const val EXTRA_SHOW = "show"
        const val EXTRA_SHOW_HOME = "show_home"
    }

    override fun getMessage(number: Int?): String? {
        return when(number){
            BuyTicketViewModel.ToastMessage.SOON.value -> getString(R.string.soon)
            else -> null
        }
    }

    private fun alert(text : String){
        AlertHelper.alertGenericTwoButtons(this,
            getString(R.string.error),
            text,
            getString(R.string.ok),
            Color.parseColor("#4089e7"),
            DialogInterface.OnClickListener { _, _ ->  },
            null, null)
    }
}