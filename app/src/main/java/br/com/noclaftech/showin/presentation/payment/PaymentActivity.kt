package br.com.noclaftech.showin.presentation.payment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityPaymentBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.AlertHelper
import br.com.noclaftech.showin.presentation.util.FacebookLog
import br.com.noclaftech.showin.presentation.util.Helper
import com.afollestad.materialdialogs.MaterialDialog
import java.io.Serializable
import javax.inject.Inject

class PaymentActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: PaymentViewModel

    override fun getBaseViewModel() = viewModel

    lateinit var binding : ActivityPaymentBinding
    private var alert: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout.activity_payment)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        viewModel.bound(intent.getFloatExtra("value", 0f), intent.getIntExtra("show", 0), intent.getIntExtra("countPack", 0))

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            setupError(it.cardHolder, viewModel.getCardHolderError())
            setupError(it.cardNumber, viewModel.getCardNumberError())
            setupError(it.cardBrand, viewModel.getCardBrandError())
            setupError(it.cardDate, viewModel.getCardDateError())
            setupError(it.cardCode, viewModel.getCardCodeError())
            setupError(it.cardDate, viewModel.getCardDateWrongFormat())

        }

//        binding.cardDate.setOnClickListener {
//            if (viewModel.cardDate.value?.length!! < 6 ){
//                binding.cardDate.error = getString(R.string.correct_format_date)
//            }
//        }

        viewModel.cardDateWrongFormat.observe(this, Observer {
            if (it){
                binding.cardDate.error = getString(R.string.invalid_format_date)
            }
        })

        viewModel.getPaymentLog().observe(this, Observer {
            if(it){
                FacebookLog.logPrePack(this)
            }
        })

        viewModel.getOpenBrand().observe(this,
            Observer {
                it?.let {
                    if (it)
                        alert = MaterialDialog.Builder(this)
                            .title(getString(R.string.brands))
                            .items(Helper.getBrands())
                            .itemsCallback { _, _, _, text ->
                                viewModel.setBrand(text.toString())
                                binding.cardBrand.setText(text.toString())
                            }
                            .show()
                }
            })

        viewModel.let {
            it.getCardBrandError().observe(this,
                Observer { bool ->
                    run {
                        if (!bool)
                            binding.cardBrand.error = null
                    }
                })
        }

        viewModel.getDetail().observe(this,
            Observer {
                it?.let {
                    setAlert(it)
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
}