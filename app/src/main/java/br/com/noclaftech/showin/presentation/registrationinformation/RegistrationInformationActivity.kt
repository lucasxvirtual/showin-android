package br.com.noclaftech.showin.presentation.registrationinformation

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R.layout
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityRegistrationInformationBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.DateDialog
import br.com.noclaftech.showin.presentation.util.Helper
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_donate_coins.view.*
import org.jetbrains.anko.toast
import javax.inject.Inject

class RegistrationInformationActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: RegistrationInformationViewModel
    //private val disposables = CompositeDisposable()
    private var alert: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding  : ActivityRegistrationInformationBinding = DataBindingUtil.setContentView(this, layout.activity_registration_information)

        screenComponent.inject(this)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
            setupError(it.name, viewModel.getNameError())
            setupError(it.birthday, viewModel.getBirthdayError())
            setupError(it.bio, viewModel.getBiographyError())

            //binding.gender.setText(Helper.convertGenderForApp(viewModel.getGender(), this))
        }

        viewModel.let {
            it.invalidDate().observe(this,
                Observer {bool->
                    if (bool) binding.birthday.error = getString(R.string.invalid_date)
                })
        }


        viewModel.biography.observe(this, Observer {
            if (it.length == 150 ){
                binding.bio.error = getString(R.string.max_length)
            }
        })

        viewModel.artisticName.observe(this, Observer {
            if (it?.length == 25 ){
                binding.name.error = getString(R.string.max_length)
            }
        })

        viewModel.getOpenGender().observe(this,
            Observer {
                it?.let {
                    if (it)
                        alert = MaterialDialog.Builder(this)
                            .title(getString(R.string.gender))
                            .items(Helper.getGender())
                            .itemsCallback { _, _, _, text ->
                                viewModel.setGender(text.toString())
                                binding.gender.setText(text.toString())
                            }
                            .show()
                }
            })

        viewModel.getOpenDate().observe(this,
            Observer{
                it?.let{
                    if(it){
                        openDate()
                    }
                }
            })
    }

    private fun openDate() {
        DateDialog.getInstance(getString(R.string.birth_date), null) {
            viewModel.updateDate(it)
        }.show(supportFragmentManager, "")

    }

    override fun getBaseViewModel() = viewModel
}