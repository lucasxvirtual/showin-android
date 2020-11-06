package br.com.noclaftech.showin.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.domain.model.Show
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ItemMyShowHorizontalBinding
import br.com.noclaftech.showin.presentation.util.Helper
import com.squareup.picasso.Picasso
import org.joda.time.Interval
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class MyShowHorizontalAdapter(private var show: List<Show>) : RecyclerView.Adapter<MyShowHorizontalAdapter.Holder>(), AdapterContract {

    lateinit var onItemClickedListener: (show: Show) -> Unit
    lateinit var onButtonClickedListener: (show: Show) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemMyShowHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClickedListener, onButtonClickedListener, parent.context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(show[position])
    }

    class Holder(
        private val binding: ItemMyShowHorizontalBinding,
        private val onItemClickListener : ((item: Show) -> Unit)?,
        private val onButtonClickListener : ((item: Show) -> Unit)?,
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
                    binding.price.visibility = View.INVISIBLE
                    binding.trashIcon.visibility = View.VISIBLE
                    binding.trashIcon.setOnClickListener { onButtonClickListener?.invoke(show) }
                }
                else -> {
                    if(show.status in listOf("CONFIG", "LIVE")) {
                        view.setBackgroundResource(R.drawable.bg_multicolor_button)
                    } else {
                        view.setBackgroundResource(R.drawable.bg_border_search)
                        binding.stream.text = daysDiff(show.date)
                    }

                    binding.price.visibility = View.VISIBLE
                    binding.stream.visibility = View.VISIBLE
                    binding.stream.setOnClickListener { onButtonClickListener?.invoke(show) }
                }
            }

            binding.name.text = show.artistArtisticName
            binding.nameShow.text = show.name
            binding.date.text = Helper.getDateShow(show.date)
            binding.hour.text = String.format("%s - %s", Helper.getHour(show.date), Helper.getHour(show.dateFinish))
            binding.priceText.text = show.ticketValue.toString()
            Picasso.get().load(show.ageRatingObj.image).into(binding.classification)
            binding.root.setOnClickListener { onItemClickListener?.invoke(show) }

        }

        private fun daysDiff(showDate: String) : String{
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val currentDate = formatter.format(Date())

            val currentDateFormat = formatter.parse(currentDate)
            val showDateFormat = formatter.parse(showDate)

            val diffSeconds = (showDateFormat!! .time - currentDateFormat!!.time) / 1000

            return getDate((diffSeconds.toFloat()))
        }

        fun getDate(seconds: Float): String {

            val d = floor(seconds / 86400)
            val h = floor((seconds / 3600) - (d * 24))
            val m = floor(seconds % 3600 / 60)

            return String.format("%.0fd %.0fh %.0fm", d, h, m)
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