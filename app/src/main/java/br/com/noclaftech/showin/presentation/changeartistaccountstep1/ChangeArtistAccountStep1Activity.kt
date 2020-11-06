package br.com.noclaftech.showin.presentation.changeartistaccountstep1

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.databinding.ActivityChangeArtistAccountStep1Binding
import br.com.noclaftech.showin.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_change_artist_account_step1.*
import javax.inject.Inject

class ChangeArtistAccountStep1Activity : BaseActivity() {

    @Inject
    lateinit var viewModel: ChangeArtistAccountStep1ViewModel
    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityChangeArtistAccountStep1Binding = DataBindingUtil.setContentView(this, layout.activity_change_artist_account_step1)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel

            setupError(it.artisticName, viewModel.getArtisticNameError())
            setupError(it.bank, viewModel.getBankError())
            setupError(it.agency, viewModel.getAgencyError())
            setupError(it.account, viewModel.getAccountError())
            setupError(it.cpf, viewModel.getCpfError())
            setupError(it.cnpj, viewModel.getCnpjError())
            setupError(it.owner, viewModel.getOwnerError())

        }

        viewModel.artisticName.observe(this, Observer {
            if (it.length == 25){
                binding.artisticName.error = getString(R.string.max_length)
            }
        })

        viewModel.let {
            it.invalidCpf().observe(this,
                Observer { bool ->
                    if (bool) binding.cpf.error = getString(R.string.invalid_cpf)
                })

            it.invalidCnpj().observe(this,
                Observer { bool ->
                    if (bool) binding.cnpj.error = getString(R.string.invalid_cnpj)
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
}