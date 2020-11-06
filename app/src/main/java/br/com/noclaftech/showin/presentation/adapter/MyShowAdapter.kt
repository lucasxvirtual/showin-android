package br.com.noclaftech.showin.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ItemMyShowBinding
import br.com.noclaftech.showin.presentation.util.Helper
import com.squareup.picasso.Picasso

class MyShowAdapter(private var show: List<Show>) :
    RecyclerView.Adapter<MyShowAdapter.Holder>(), AdapterContract {

    lateinit var onItemClickedListener: (show: Show) -> Unit
    lateinit var onButtonClickedListener: (show: Show) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemMyShowBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClickedListener, onButtonClickedListener, parent.context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(show[position])
    }

    class Holder(
        private val binding: ItemMyShowBinding,
        private val onItemClickListener : ((item: Show) -> Unit)?,
        private val onButtonClickedListener : ((item: Show) -> Unit)?,
        private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var show: Show

        fun bind(show: Show) {
            this.show = show

            binding.viewHolder = this

            val view: View = binding.stream

            when(show.status){
                "CANCELED", "DONE" -> {
                    view.setBackgroundColor(Color.TRANSPARENT)
                    binding.coin.visibility = View.INVISIBLE
                    binding.price.visibility = View.INVISIBLE
                    binding.stream.visibility = View.VISIBLE
                    binding.stream.text = context.getString(R.string.delete)
                    binding.stream.setOnClickListener { onButtonClickedListener?.invoke(show) }
                }
                else -> {
                    if(show.status in listOf("CONFIG", "LIVE"))
                        view.setBackgroundResource(R.drawable.bg_button)
                    else
                        view.setBackgroundResource(R.drawable.bg_border_search)
                    binding.coin.visibility = View.VISIBLE
                    binding.price.visibility = View.VISIBLE
                    binding.stream.visibility = View.VISIBLE
                    binding.stream.text = context.getString(R.string.start)
                    binding.stream.setOnClickListener { onButtonClickedListener?.invoke(show) }
                }
            }

            binding.name.text = show.artistArtisticName
            binding.nameShow.text = show.name
            binding.date.text = Helper.getDateShow(show.date)
            binding.hour.text = "${Helper.getHour(show.date)} - ${Helper.getHour(show.dateFinish)}"
            binding.price.text = show.ticketValue.toString()
            Picasso.get().load(show.ageRatingObj.image).into(binding.classification)
            binding.root.setOnClickListener { onItemClickListener?.invoke(show) }
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