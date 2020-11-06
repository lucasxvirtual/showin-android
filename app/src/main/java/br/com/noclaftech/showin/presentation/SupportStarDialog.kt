package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import br.com.noclaftech.showin.R
import kotlinx.android.synthetic.main.dialog_support_star.view.*

class SupportStarDialog(val callback: ((winnsAmount: Int, password: String) -> Unit)? = null) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.dialog_support_star, null)

            view.button.setOnClickListener {
                if (view.winns_amount.text.isNullOrBlank()){
                    view.winns_amount.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                if (view.password.text.isNullOrBlank()){
                    view.password.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                callback?.invoke(
                    view.winns_amount.text.toString().toInt(),
                    view.password.text.toString()
                )
                dismiss()

            }

            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialogTransparent)
            builder.setView(view)

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

}