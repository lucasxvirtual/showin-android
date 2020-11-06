package br.com.noclaftech.showin.presentation.paymentinfo

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityPaymentInfoBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.util.AlertHelper
import javax.inject.Inject

class PaymentInfoActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: PaymentInfoViewModel
    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityPaymentInfoBinding = DataBindingUtil.setContentView(this, layout.activity_payment_info)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        viewModel.bound()

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this

            setupError(it.bank, viewModel.getBankError())
            setupError(it.agency, viewModel.getAgencyError())
            setupError(it.account, viewModel.getAccountError())
            setupError(it.cpf, viewModel.getCpfError())
            setupError(it.cnpj, viewModel.getCnpjError())
            setupError(it.owner, viewModel.getOwnerError())
        }

        viewModel.let {
            it.invalidCpf().observe(this,
                Observer { bool ->
                    if (bool) binding.cpf.error = getString(R.string.invalid_cpf)
                })

            it.invalidCnpj().observe(this,
                Observer { bool ->
                    if (bool) binding.cnpj.error = getString(R.string.invalid_cnpj)
                })

            it.getDetail().observe(this,
                Observer {
                    it?.let {
                        setAlert(it)
                    }
                })
        }

        binding.spinner.adapter = ArrayAdapter.createFromResource(this, R.array.legal_or_physical_person, android.R.layout.simple_spinner_item).also {
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) } as SpinnerAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.getItemAtPosition(position).toString()

                when (position){
                    0 -> {
                        viewModel.setPhysicalPerson()
                    }
                    1 -> {
                        viewModel.setLegalPerson()
                    }

                }
            }

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