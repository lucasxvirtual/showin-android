package br.com.noclaftech.showin.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.ItemExtractBinding
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.domain.model.Extract

class ExtractAdapter(private var extracts: List<Extract>, private val context : Context) : RecyclerView.Adapter<ExtractAdapter.Holder>(), AdapterContract{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemExtractBinding.inflate(LayoutInflater.from(parent.context), parent, false), context)
    }

    override fun getItemCount(): Int {
        return extracts.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(extracts[position])
    }

    override fun replaceItems(items: List<*>) {
        this.extracts = items.filterIsInstance<Extract>() as ArrayList<Extract>
        notifyDataSetChanged()
    }

    fun addItems(items: List<*>){
//        val list = arrayListOf<Extract>()
//        list.addAll(extracts)
//        list.addAll(items.filterIsInstance<Extract>() as ArrayList<Extract>)

        this.extracts = items.filterIsInstance<Extract>() as ArrayList<Extract>
        notifyDataSetChanged()
    }

    class Holder(private val binding : ItemExtractBinding, private val context : Context): RecyclerView.ViewHolder(binding.root) {
        private lateinit var extract: Extract

        fun bind(extract: Extract) {
            this.extract = extract

            if(extract.createdAt != null)
                binding.date.text = Helper.getDateTimeFormaterApp(extract.createdAt!!)
            else
                binding.date.text = ""
            val isIncome = extract.balanceTo != null && extract.balanceTo!!.value != null


            var owner : String? = null
            if (isIncome){
                if(extract.value != null) {
                    binding.totalCoin.text = "+ ${extract.value}"
                    binding.iconCoin.visibility = View.VISIBLE
                } else {
                    binding.totalCoin.text = "R$ ${String.format("%.2f", extract.price).replace(".", ",")}"
                    binding.iconCoin.visibility = View.GONE
                }
                binding.totalCoin.setTextColor(context.getColor(R.color.light_blue))
                binding.iconCoin.setImageDrawable(context.getDrawable(R.drawable.coin_icon_blue))
                if(extract.balanceFrom != null)
                    owner = extract.balanceFrom!!.balanceOwner
            } else {
                if(extract.value != null) {
                    binding.totalCoin.text = "- ${extract.value}"
                    binding.iconCoin.visibility = View.VISIBLE
                } else {
                    binding.totalCoin.text = "R$ ${String.format("%.2f", extract.price).replace(".", ",")}"
                    binding.iconCoin.visibility = View.GONE
                }
                binding.totalCoin.setTextColor(context.getColor(R.color.red))
                binding.iconCoin.setImageDrawable(context.getDrawable(R.drawable.coin_icon_red))
                if(extract.balanceTo != null)
                    owner = extract.balanceTo!!.balanceOwner
            }

            binding.type.text = "${getReasonText(extract.reason, isIncome, owner)} "
        }

        private fun getReasonText(reason: String, isIncome: Boolean, owner: String?): String{
            val mOwner = owner ?: ""
            return when(reason){
                "TICKET_PURCHASE", "REAL" -> {
                    String.format(context.getString(R.string.purchase), mOwner)
                }
                "SHOW_DONATION" -> {
                    String.format(context.getString(R.string.donation_to), mOwner)
                }
                "USER_DONATION" -> {
                    if(isIncome)
                        String.format(context.getString(R.string.donation_of), mOwner)
                    else
                        String.format(context.getString(R.string.donation_to), mOwner)
                }
                "COIN_PURCHASE" -> {
                    context.getString(R.string.purchase_coin)
                }
                else -> {
                    return String.format(context.getString(R.string.refund), mOwner)
                }
            }
        }
    }
}