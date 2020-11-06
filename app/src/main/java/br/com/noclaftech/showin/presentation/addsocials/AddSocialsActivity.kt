package br.com.noclaftech.showin.presentation.addsocials

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ActivityAddSocialsBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.showin.presentation.BaseViewModel
import br.com.noclaftech.showin.presentation.util.AlertHelper
import kotlinx.android.synthetic.main.activity_add_socials.*
import javax.inject.Inject

class AddSocialsActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: AddSocialsViewModel

    override fun getBaseViewModel()= viewModel

    lateinit var binding: ActivityAddSocialsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_socials)

        screenComponent.inject(this)

        binding.let{
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

//        binding.whatsapp.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                binding.whatsapp.hint = getString(R.string.whatsapp_hint)
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//        })
//
        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged.invoke(editable.toString())
                }
            })
        }

        binding.instagram.afterTextChanged {
            if (it.isNotEmpty()) {
                binding.instagramAtSign.setTextColor(getColor(R.color.white))
            } else {
                binding.instagramAtSign.setTextColor(getColor(android.R.color.darker_gray))
            }
        }

        binding.whatsapp.afterTextChanged {
            if (it.isNotEmpty()){
                binding.whatsappHolder.setTextColor(getColor(R.color.white))
            } else {
                binding.whatsappHolder.setTextColor(getColor(android.R.color.darker_gray))
            }
        }

        binding.facebook.afterTextChanged {
            if (it.isNotEmpty()){
                binding.facebookHolder.setTextColor(getColor(R.color.white))
            } else {
                binding.facebookHolder.setTextColor(getColor(android.R.color.darker_gray))
            }
        }

        binding.linkedin.afterTextChanged {
            if (it.isNotEmpty()){
                binding.linkedinHolder.setTextColor(getColor(R.color.white))
            } else {
                binding.linkedinHolder.setTextColor(getColor(android.R.color.darker_gray))
            }
        }

        binding.twitter.afterTextChanged {
            if (it.isNotEmpty()){
                binding.twitterAtSign.setTextColor(getColor(R.color.white))
            } else {
                binding.twitterAtSign.setTextColor(getColor(android.R.color.darker_gray))
            }
        }

        binding.facebookLayout.setOnClickListener {
//            binding.facebook.isFocusableInTouchMode = true
            binding.facebook.requestFocus()
        }

        viewModel.let {
            it.getDetail().observe(this,
                Observer {
                    it?.let { string ->
                        setAlert(string)
                    }
                })
        }

        viewModel.getWhatsappError().observe(this,
            Observer { bool ->
                if (bool)
                    setAlert(getString(R.string.invalid_phone))
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