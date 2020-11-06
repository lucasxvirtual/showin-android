package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import br.com.noclaftech.domain.model.Ticket
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.presentation.tickets.TicketsViewModel
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.dialog_date.view.*
import kotlinx.android.synthetic.main.dialog_donate_tickets.*
import kotlinx.android.synthetic.main.dialog_donate_tickets.view.*
import kotlinx.android.synthetic.main.dialog_donate_tickets.view.donate_button
import kotlinx.android.synthetic.main.dialog_transfer_winns.view.*

class TransferWinnsDialog(val callback: ((Triple<Int, String, String>) -> Unit)?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.dialog_transfer_winns, null)

            view.transferButton.setOnClickListener {
                if (view.winnsQuantityEditText.text.isNullOrBlank()){
                    view.select_ticket.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                if (view.usernameEditText.text.isNullOrBlank()){
                    view.select_ticket.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                if(view.passwordEditText.text.isNullOrBlank()){
                    view.passwordEditText.error =  getString(R.string.required_field)
                    return@setOnClickListener
                }

                callback?.invoke(
                    Triple(
                        view.winnsQuantityEditText.text.toString().toInt(),
                        view.usernameEditText.text.toString(),
                        view.passwordEditText.text.toString()
                    )
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