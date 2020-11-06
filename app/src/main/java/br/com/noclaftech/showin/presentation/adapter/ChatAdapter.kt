package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.domain.model.ChatMessage
import br.com.noclaftech.showin.databinding.ItemChatBinding
import br.com.noclaftech.showin.databinding.ItemDonationBinding

class ChatAdapter(private var messages : List<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.Holder>(), AdapterContract{

    lateinit var onItemClickedListener: (message: ChatMessage) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return if (viewType == 0)
            Holder(ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent,false), onItemClickedListener)
        else
            Holder(ItemDonationBinding.inflate(LayoutInflater.from(parent.context), parent,false), onItemClickedListener)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun getItemViewType(position: Int): Int {
        if(messages[position].messageType == "donation")
            return 1
        return 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(messages[position].messageType == "donation")
            holder.bindDonation(messages[position])
        else
            holder.bind(messages[position])
    }

    override fun replaceItems(items: List<*>) {
        this.messages = items.filterIsInstance<ChatMessage>() as ArrayList<ChatMessage>
        notifyDataSetChanged()
    }

    class Holder : RecyclerView.ViewHolder{

        private  var binding : ItemChatBinding? = null
        private  var bindingDonation : ItemDonationBinding? = null
        private val onItemClickedListener: (message: ChatMessage) -> Unit

        constructor(binding: ItemChatBinding, onItemClickedListener: (message: ChatMessage) -> Unit) : super(binding.root) {
            this.binding = binding
            this.onItemClickedListener = onItemClickedListener
        }

        constructor(binding: ItemDonationBinding, onItemClickedListener: (message: ChatMessage) -> Unit) : super(binding.root){
            this.bindingDonation = binding
            this.onItemClickedListener = onItemClickedListener
        }

        lateinit var message: ChatMessage

        fun bind(message: ChatMessage) {
            this.message = message
            this.binding?.viewHolder = this

            this.binding?.moreImageView?.setOnClickListener {
                onItemClickedListener.invoke(message)
            }
        }

        fun bindDonation(message: ChatMessage){
            this.message = message
            this.bindingDonation?.viewHolder = this

            this.binding?.root?.setOnClickListener {
                onItemClickedListener.invoke(message)
            }
        }
    }
}