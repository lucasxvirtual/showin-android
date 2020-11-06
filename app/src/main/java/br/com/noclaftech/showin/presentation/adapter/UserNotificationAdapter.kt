package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.domain.model.Extract
import br.com.noclaftech.domain.model.Notification
import br.com.noclaftech.showin.databinding.ItemUserNotificationBinding
import br.com.noclaftech.showin.presentation.util.Helper

class UserNotificationAdapter(private var notifications: List<Notification>) : RecyclerView.Adapter<UserNotificationAdapter.Holder>(), AdapterContract {

    lateinit var onItemClickedListener: (notification: Notification) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemUserNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClickedListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(notifications[position])
    }

    class Holder(
        private val binding: ItemUserNotificationBinding,
        private val onItemClickListener: (notification: Notification) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var notification: Notification

        fun bind(notification: Notification) {
            this.notification = notification
            binding.message.text = notification.message
            binding.date.text = Helper.getDateForNotification(notification.createdAt)
            binding.deleteButton.setOnClickListener { onItemClickListener(notification) }
        }
    }

    override fun replaceItems(items: List<*>) {
        this.notifications = items.filterIsInstance<Notification>() as ArrayList<Notification>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return notifications.count()
    }

    fun addItems(items: List<*>){
       this.notifications = items.filterIsInstance<Notification>() as ArrayList<Notification>
        notifyDataSetChanged()
    }
}