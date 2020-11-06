package br.com.noclaftech.showin.presentation.scheduleshow

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityScheduleShowStep3Binding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import javax.inject.Inject

class ScheduleShowStep3Activity : BaseActivity() {

    @Inject
    lateinit var viewModel : ScheduleShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityScheduleShowStep3Binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_show_step3)

        screenComponent.inject(this)

        viewModel.setSuccessShow(intent.getSerializableExtra("success")!!)

        binding.viewModel = viewModel

        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        viewModel.getSuccessShow().observe(this,
            Observer {
                it?.let {
                    binding.date.text = viewModel.getDate()
                    binding.hour.text = viewModel.getHour()
                }
            })
    }

    override fun getBaseViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onBackPressed() {
        viewModel.onClickFinish()
    }
}
