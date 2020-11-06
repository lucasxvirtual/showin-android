package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.databinding.ItemArtistBinding
import br.com.noclaftech.domain.model.Artist

class ArtistAdapter(private var artist: List<Artist>) : RecyclerView.Adapter<ArtistAdapter.Holder>(), AdapterContract {

    lateinit var onItemClickedListener: (artist: Artist) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClickedListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(artist[position])
    }

    class Holder(
        private val binding: ItemArtistBinding,
        private val onItemClickListener : ((item: Artist) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var artist: Artist

        fun bind(artist: Artist) {
            this.artist = artist
            binding.viewHolder = this
            binding.nameArtist.text = artist.artisticName
            //Picasso.get().load(artist.user.profileImage).resize(1000, 0).into(binding.image)

            binding.root.setOnClickListener { onItemClickListener?.invoke(artist)  }
        }
    }

    override fun replaceItems(items: List<*>) {
        this.artist = items.filterIsInstance<Artist>() as ArrayList<Artist>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return artist.count()
    }
}