package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.presentation.util.Helper
import kotlinx.android.synthetic.main.dialog_hour.view.*

class HourDialog : DialogFragment() {

    var callback: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.dialog_hour, null)

            view.title.text = arguments?.get("title") as String

            val subtitle = arguments?.get("subtitle") as String?

            if(subtitle != null){
                view.subtitle.text = subtitle
                view.subtitle.visibility = View.VISIBLE
            }

            view.hour.addTextChangedListener(MaskEditTextChangedListener("##:##", view.hour))

            view.ok.setOnClickListener {
                if(view.hour.text.isNullOrBlank()){
                    view.hour.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                if (!Helper.validateHour(view.hour.text.toString())){
                    view.hour.error = getString(R.string.insert_a_valid_hour)
                    return@setOnClickListener
                }


                val string = view.hour.text.toString()
                callback?.invoke(string)
                dismiss()
            }

            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialogTransparent)
            builder.setView(view)

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    companion object {
        fun getInstance(title: String, subtitle: String?, callback: ((String) -> Unit)?): HourDialog{
            val bundle = bundleOf("title" to title, "subtitle" to subtitle)
            return HourDialog().apply {
                arguments = bundle
                this.callback = callback
            }
        }
    }

}