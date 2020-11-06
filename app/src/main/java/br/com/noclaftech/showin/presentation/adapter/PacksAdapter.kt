package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ItemBuyCoinBinding
import br.com.noclaftech.showin.presentation.BaseActivity
import br.com.noclaftech.domain.model.Pack

class PacksAdapter(private var extracts: List<Pack>, private val activity : BaseActivity) : RecyclerView.Adapter<PacksAdapter.Holder>(), AdapterContract{
    lateinit var onButtonClickedListener: (pack: Pack) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemBuyCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false), activity, onButtonClickedListener)
    }

    override fun getItemCount(): Int {
        return extracts.count()
    }

    override fun onBindViewHolder(holder: PacksAdapter.Holder, position: Int) {
        holder.bind(extracts[position])
    }

    override fun replaceItems(items: List<*>) {
        this.extracts = items.filterIsInstance<String>() as ArrayList<Pack>
        notifyDataSetChanged()
    }

    class Holder(private val binding : ItemBuyCoinBinding,
                 private val activity: BaseActivity,
                 private val onButtonClickListener : ((item: Pack) -> Unit)?): RecyclerView.ViewHolder(binding.root) {

        private lateinit var pack: Pack

        fun bind(pack: Pack) {
            this.pack = pack
            binding.totalCoins.text = "${pack.coin_amout} ${activity.getString(R.string.winns)}"
            binding.buttonPrice.text = "${activity.getString(R.string.money_type)} ${pack.price}"

            binding.buttonPrice.setOnClickListener { onButtonClickListener?.invoke(pack) }
        }
    }
}