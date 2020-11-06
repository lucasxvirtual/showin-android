package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.databinding.ItemTicketBinding
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.domain.model.Ticket
import br.com.noclaftech.showin.databinding.ItemTicketOldBinding

class TicketsOldAdapter(private var ticket: List<Ticket>):
    RecyclerView.Adapter<TicketsOldAdapter.Holder>(), AdapterContract{

    lateinit var onItemClickedListener: (ticket: Ticket) -> Unit
    lateinit var onButtonClickedListener: (ticket: Ticket) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemTicketOldBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClickedListener, onButtonClickedListener
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(ticket[position])
    }

    class Holder(
        val binding: ItemTicketOldBinding,
        private val onItemClickListener : ((item: Ticket) -> Unit)?,
        private val onButtonClickedListener: ((item: Ticket) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var ticket: Ticket

        fun bind(ticket: Ticket) {
            this.ticket = ticket

            binding.viewHolder = this

            binding.name.text = ticket.show.artistArtisticName
            binding.nameShow.text = ticket.show.name
            binding.date.text = Helper.getDateShow(ticket.show.date)
            binding.hour.text = "${Helper.getHour(ticket.show.date)} - ${Helper.getHour(ticket.show.dateFinish)}"
            //Picasso.get().load(ticket.show.horizontalImage).into(binding.image)
            binding.deleteTicket.setOnClickListener { onButtonClickedListener?.invoke(ticket) }

            binding.root.setOnClickListener { onItemClickListener?.invoke(ticket) }

        }
    }

    override fun getItemCount(): Int {
        return ticket.count()
    }

    override fun replaceItems(items: List<*>) {
        this.ticket = items.filterIsInstance<Ticket>() as ArrayList<Ticket>
        notifyDataSetChanged()

    }
}