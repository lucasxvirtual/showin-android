package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import br.com.noclaftech.showin.R
import kotlinx.android.synthetic.main.dialog_end_stream.view.*

class EndStreamDialog(val callback : () -> Unit?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.dialog_end_stream, null)

            view.cancelButton.setOnClickListener {
                dismiss()
            }

            view.finishButton.setOnClickListener {
                callback.invoke()
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