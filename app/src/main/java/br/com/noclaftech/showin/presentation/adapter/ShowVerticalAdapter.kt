package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ItemShowVerticalBinding
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.domain.model.Show
import com.squareup.picasso.Picasso

class ShowVerticalAdapter(private var show: List<Show>) :
    RecyclerView.Adapter<ShowVerticalAdapter.Holder>(), AdapterContract {

    lateinit var onItemClickedListener: (show: Show) -> Unit
    lateinit var onButtonClickedListener: (show: Show) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemShowVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClickedListener, onButtonClickedListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(show[position])
    }

    class Holder(
        private val binding: ItemShowVerticalBinding,
        private val onItemClickListener : ((item: Show) -> Unit)?,
        private val onButtonClickListener : ((item: Show) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var show: Show

        fun bind(show: Show) {
            this.show = show

            binding.viewHolder = this

            binding.name.text = show.artistArtisticName
            binding.nameShow.text = show.name
            binding.date.text = Helper.getDateShow(show.date)
            binding.hour.text = "${Helper.getHour(show.date)} - ${Helper.getHour(show.dateFinish)}"
            binding.price.text = show.ticketValue.toString()
            Picasso.get().load(show.ageRatingObj.image).into(binding.classification)

            binding.root.setOnClickListener { onItemClickListener?.invoke(show) }
            binding.buy.setOnClickListener { onButtonClickListener?.invoke(show) }
        }
    }

    override fun replaceItems(items: List<*>) {
        this.show = items.filterIsInstance<Show>() as ArrayList<Show>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return show.count()
    }
}