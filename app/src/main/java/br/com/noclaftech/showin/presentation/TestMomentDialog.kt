package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import br.com.noclaftech.showin.R
import kotlinx.android.synthetic.main.dialog_test_moment.view.*

class TestMomentDialog(val callback : () -> Unit?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.dialog_test_moment, null)

            view.startTestButton.setOnClickListener {
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