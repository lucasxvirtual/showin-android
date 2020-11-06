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

class DonateTicketsDialog(val list : List<Ticket>, val count : Map<Int, List<Ticket>>,
                          val callback: ((Triple<String, Int, Int>) -> Unit)?) : DialogFragment() {

    private var selectedTicketId = 0
    private var selectedTicketCount = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.dialog_donate_tickets, null)

            view.select_ticket.setOnClickListener {
                context?.let { ctx ->
                    MaterialDialog.Builder(ctx)
                        .title(getString(R.string.select_the_ticket))
                        .items(list.map {
                            it.show.name
                        })
                        .itemsCallback { _, _, pos, text ->
                            selectedTicketId = list[pos].show.id
                            view.select_ticket.setText(text)
                        }
                        .show()
                }
            }

            view.ticket_count_select.setOnClickListener {
                context?.let { ctx ->
                    if(selectedTicketId == 0) {
                        Toast.makeText(ctx, getString(R.string.select_the_ticket), Toast.LENGTH_SHORT).show()
                    } else {

                        MaterialDialog.Builder(ctx)
                            .title(R.string.how_many_tickets)
                            .items((1..count[selectedTicketId]?.size!!).toList())
                            .itemsCallback { _, _, _, text ->
                                selectedTicketCount = text.toString().toInt()
                                view.ticket_count_select.setText(text)
                            }
                            .show()
                    }
                }

            }

            view.donate_button.setOnClickListener {
                if (view.select_ticket.text.isNullOrBlank()){
                    view.select_ticket.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                if (view.ticket_count_select.text.isNullOrBlank()){
                    view.select_ticket.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                if (view.addressee_username.text.isNullOrBlank()){
                    view.addressee_username.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                val infoDonate =  Triple(view.addressee_username.text.toString().replace("@", ""), selectedTicketId, selectedTicketCount)
                callback?.invoke(infoDonate)
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