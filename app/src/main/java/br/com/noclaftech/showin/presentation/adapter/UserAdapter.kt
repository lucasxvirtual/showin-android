package br.com.noclaftech.showin.presentation.adapter

import br.com.noclaftech.domain.model.User
import br.com.noclaftech.showin.databinding.ItemUserBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.domain.model.Artist
import com.squareup.picasso.Picasso

class UserAdapter(private var user: List<User>) : RecyclerView.Adapter<UserAdapter.Holder>(), AdapterContract {

    lateinit var onItemClickedListener: (user: User) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClickedListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(user[position])
    }

    class Holder(
        private val binding: ItemUserBinding,
        private val onItemClickListener : ((item: User) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var user: User

        fun bind(user: User) {
            this.user = user
            binding.viewHolder = this
            binding.nameArtist.text = user.name

            Picasso.get()
                .load(user.profileImage)
                .resize(150, 0)
                .centerCrop()
                .into(binding.image)

            binding.root.setOnClickListener { onItemClickListener?.invoke(user)  }
        }
    }

    override fun replaceItems(items: List<*>) {
        this.user = items.filterIsInstance<User>() as ArrayList<User>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return user.count()
    }
}