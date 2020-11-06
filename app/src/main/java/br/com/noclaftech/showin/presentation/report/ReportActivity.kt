package br.com.noclaftech.showin.presentation.report

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityReportBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import javax.inject.Inject

class ReportActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: ReportViewModel
    lateinit var binding : ActivityReportBinding

    override fun getBaseViewModel() = viewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report)

        screenComponent.inject(this)

        viewModel.setInfos(intent.extras!!["id"] as Int, intent.extras!!["message"] as String)

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
            setupError(it.denounce, viewModel.getReportError())
        }


        viewModel.getShowMessage().observe(this,
            Observer {
                it?.let {
                    if (it)
                        Toast.makeText(this, getString(R.string.report_sucess_sent), Toast.LENGTH_LONG).show()
                }
            })
    }
}