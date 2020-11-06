package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.databinding.ItemTicketBinding
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.domain.model.Ticket
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.presentation.BaseActivity

class TicketsAdapter(private var ticket: List<Ticket>, val count : Map<Int, List<Ticket>>,private val activity : BaseActivity) :
    RecyclerView.Adapter<TicketsAdapter.Holder>(), AdapterContract{

    lateinit var onItemClickedListener: (ticket: Ticket) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false),activity, onItemClickedListener, count
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(ticket[position])
    }

    class Holder(
        val binding: ItemTicketBinding,
        private val activity: BaseActivity,
        private val onItemClickListener : ((item: Ticket) -> Unit)?,
        private val count: Map<Int, List<Ticket>>):
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

            val count = count[ticket.show.id]?.size.toString().toInt()
            if (count <= 1){
                binding.ticketsCount.text = "${String.format(activity.getString(R.string.one_ticket), count)}"
            } else {
                binding.ticketsCount.text = "${String.format(activity.getString(R.string.ticket_plural), count)}"
            }
//            binding.ticketsCount.text = ticket.count.toString()
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