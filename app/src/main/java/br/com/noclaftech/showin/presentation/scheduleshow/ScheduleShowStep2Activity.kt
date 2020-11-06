package br.com.noclaftech.showin.presentation.scheduleshow

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityScheduleShowStep2Binding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.CostDetailDialog
import br.com.noclaftech.showin.presentation.util.Helper
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_schedule_show_step2.*
import storage.Singleton
import javax.inject.Inject

class ScheduleShowStep2Activity : BaseActivity() {

    @Inject
    lateinit var viewModel: ScheduleShowViewModel
    private var alert: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityScheduleShowStep2Binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_show_step2)

        screenComponent.inject(this)

        viewModel.setShow(intent.getSerializableExtra("show")!!)

        binding.viewModel = viewModel
        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            setupError(it.ticketPriceCoins, viewModel.getTicketError())
            setupError(it.suggestedCurrencyValue, viewModel.getSuggestedCurrencyError())
            setupError(it.ticketLimit, viewModel.getLimitError())
        }

        viewModel.getInvalidSuggestedCurrency().observe(this,
            Observer {
                it?.let {bool ->
                    if(bool) binding.suggestedCurrencyValue.error = getString(R.string.value_minimum_ticket, Singleton.instance.getConstants()?.blockingGet()!!.config.minimunPrice!!)

                }
            })

        viewModel.getInvalidTicket().observe(this,
            Observer {
                it?.let {bool ->
                    if(bool) binding.ticketPriceCoins.error = getString(R.string.value_minimum_ticket, Singleton.instance.getConstants()?.blockingGet()!!.config.minimunPrice!!)

                }
            })

//        viewModel.getOpenCategory().observe(this,
//            Observer {
//                it?.let {
//                    if (it)
//                        alert = MaterialDialog.Builder(this)
//                            .title(getString(R.string.category))
//                            .items(Helper.getCategories())
//                            .itemsCallback { _, _, _, text ->
//                                viewModel.setCategory(text.toString())
//                            }
//                            .show()
//                }
//            })

        viewModel.getCost().observe(this,
            Observer {
                it?.let {
                    if (it)
                        CostDetailDialog().show(supportFragmentManager, "")
                }
            })

        binding.ticketPriceCoins.addTextChangedListener(){
            if (it.isNullOrEmpty()){
                setRevenueDefaultValues()
            } else {
                viewModel.postShowRevenue()
            }
        }

        binding.suggestedCurrencyValue.addTextChangedListener(){
            if (it.isNullOrEmpty()){
                setRevenueDefaultValues()
            } else {
                viewModel.postShowRevenue()
            }
        }

        viewModel.coinsPrice.observe(this, Observer {
            it.let {
                if (it != null) {
                    binding.showinValue.text = String.format(getString(R.string.tax), (it.coin_price.showinPercentage.toString() + "%"))
                    binding.remainingTicketValue.text = String.format(getString(R.string.price_value_float), it.coin_price.showinValue).replace(".", ",")
                    binding.artistValue.text = String.format(getString(R.string.ticket_value_artist), (it.coin_price.percentageArtist.toString() + "%"))
                    binding.artistValuePerTicket.text = String.format(getString(R.string.price_value_float), it.coin_price.artistValuePerTicket).replace(".", ",")
                }
            }
        })
    }

    private fun setRevenueDefaultValues(){
        remaining_ticket_value.text = getString(R.string.price_value_zero)
        artist_value_per_ticket.text = getString(R.string.price_value_zero)
    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }
}