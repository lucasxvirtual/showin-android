package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.domain.model.ArtistExtract
import br.com.noclaftech.showin.databinding.ItemExtractArtistBinding

class ExtractArtistAdapter(private var extracts: List<ArtistExtract>) : RecyclerView.Adapter<ExtractArtistAdapter.Holder>(), AdapterContract{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemExtractArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return extracts.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(extracts[position])
    }

    override fun replaceItems(items: List<*>) {
        this.extracts = items.filterIsInstance<ArtistExtract>() as ArrayList<ArtistExtract>
        notifyDataSetChanged()
    }

    class Holder(private val binding : ItemExtractArtistBinding): RecyclerView.ViewHolder(binding.root) {
        lateinit var extract: ArtistExtract

        fun bind(extract: ArtistExtract) {
            this.extract = extract
            binding.viewHolder = this
        }

        fun getValuePaidTickets() = String.format("%.2f", extract.valuePaidTickets.toFloat() + extract.valueDonations.toFloat()).replace(".", ",")
        fun getPlatformCost() = String.format("%.2f", extract.platformCost).replace(".", ",")
        fun getEcadCost() = String.format("%.2f", extract.ecadCost).replace(".", ",")
        fun getArtistTotal() = String.format("%.2f", extract.artistTotal).replace(".", ",")
        fun getPercentage() = String.format("%d%%", extract.percentage?.times(100)!!.toInt())
        fun getValuePaidTicketsReal() = String.format("%.2f", extract.valuePaidTicketsReal).replace(".", ",")
        fun getValueDonationsReal() = String.format("%.2f", extract.valueDonationsReal).replace(".", ",")
        fun getArtistTotalReal() = String.format("%.2f", extract.artistTotalReal).replace(".", ",")
        fun getShowinValue() = String.format("%.2f", extract.showinValue).replace(".", ",")
        fun getArtistPercentage() = String.format("%d%%", extract.artistPercentage!!.toInt())
        fun getShowinPercentage() = String.format("%d%%", extract.showinPercentage!!.toInt())
        fun getTotal() = String.format("%.2f",
            extract.valueDonationsReal?.let { extract.valuePaidTicketsReal?.plus(it) }).replace(".", ",")
    }
}