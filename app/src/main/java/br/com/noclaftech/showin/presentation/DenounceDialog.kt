package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import br.com.noclaftech.domain.model.ChatMessage
import br.com.noclaftech.showin.R
import kotlinx.android.synthetic.main.dialog_denounce.view.*

class DenounceDialog(val message: ChatMessage, val callback: ((userId : Int, message : String, reason : String) -> Unit)?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.dialog_denounce, null)

            view.subtitle.text = String.format(getString(R.string.user), message.username)
            view.subtitle2.text = String.format(getString(R.string.denounce_message), message.message)

            view.ok.setOnClickListener {
                if(view.reasonEditText.text.toString().isEmpty()) {
                    view.reasonEditText.error = getText(R.string.required_field)
                    return@setOnClickListener
                }

                callback?.invoke(message.id!!, message.message!!, view.reasonEditText.text.toString())
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