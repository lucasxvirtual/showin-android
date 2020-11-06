package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.domain.model.Follow
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.showin.databinding.ItemFollowersBinding
import com.squareup.picasso.Picasso

class FollowersAdapter (private var followers : List<Follow>) :
    RecyclerView.Adapter<FollowersAdapter.Holder>(), AdapterContract {

    lateinit var onItemClickedListener: (follow: Follow) -> Unit

    class Holder(private val binding: ItemFollowersBinding, private val onItemClickListener : ((item: Follow) -> Unit)?) : RecyclerView.ViewHolder(binding.root){

        private lateinit var followers: Follow

        fun bind( follow: Follow ){
            this.followers = follow
            binding.follow = follow
            binding.root.setOnClickListener { onItemClickListener?.invoke(follow) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClickedListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(followers[position])
    }

    override fun replaceItems(items: List<*>) {
        this.followers = items.filterIsInstance<Follow>() as ArrayList<Follow>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return followers.count()
    }

    fun addItems(items: List<*>){
        this.followers = items.filterIsInstance<Follow>() as ArrayList<Follow>
        notifyDataSetChanged()
    }
}