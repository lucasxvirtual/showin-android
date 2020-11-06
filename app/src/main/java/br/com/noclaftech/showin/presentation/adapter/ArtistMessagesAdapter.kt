package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.domain.model.Message
import br.com.noclaftech.showin.databinding.ItemArtistMessageBinding
import br.com.noclaftech.showin.presentation.util.Helper

class ArtistMessagesAdapter (private var artistMessage: List<Message>) :
    RecyclerView.Adapter<ArtistMessagesAdapter.Holder>(), AdapterContract {

    class Holder (
        private val binding: ItemArtistMessageBinding) : RecyclerView.ViewHolder(binding.root){

        private lateinit var artistMessage: Message

        fun bind(artistMessage: Message){
            this.artistMessage = artistMessage

            binding.artistMessage = artistMessage
            binding.messageDate.text = Helper.getDateForMessage(artistMessage.created_at)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder (ItemArtistMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(artistMessage[position])
    }

    override fun replaceItems(items: List<*>) {
        this.artistMessage = items.filterIsInstance<Message>() as ArrayList<Message>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return artistMessage.count()
    }
}