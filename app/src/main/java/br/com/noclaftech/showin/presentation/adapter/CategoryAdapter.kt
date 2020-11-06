package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.noclaftech.domain.model.Category
import br.com.noclaftech.showin.databinding.ItemCategoryBinding

class CategoryAdapter(private var categories : List<Category>) : RecyclerView.Adapter<CategoryAdapter.Holder>(), AdapterContract{

    lateinit var onItemClickedListener: (category: Category) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent,false), onItemClickedListener)
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(categories[position])
    }

    override fun replaceItems(items: List<*>) {
        this.categories = items.filterIsInstance<Category>() as ArrayList<Category>
        notifyDataSetChanged()
    }

    class Holder(private val binding: ItemCategoryBinding, private val onItemClickedListener: (category: Category) -> Unit) : RecyclerView.ViewHolder(binding.root){
        lateinit var category: Category

        fun bind(category: Category) {
            this.category = category
            this.binding.viewHolder = this
        }

        fun onClick(){
            onItemClickedListener.invoke(category)
        }
    }
}