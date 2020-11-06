package br.com.noclaftech.showin.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import br.com.noclaftech.domain.model.Banner
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.databinding.PageItemBinding
import br.com.noclaftech.showin.presentation.util.Helper
import br.com.noclaftech.domain.model.Show
import com.squareup.picasso.Picasso

open class PageAdapter(private var lst: List<Banner>): PagerAdapter() {

    lateinit var onItemClickedListener: (banner: Banner) -> Unit
    lateinit var onButtonClickedListener : (banner: Banner) -> Unit

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as ConstraintLayout
    }

    override fun getCount(): Int {
        return lst.size
    }

    fun setItems(items: List<Banner>){
        lst = items
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)

        val binding = PageItemBinding.inflate(inflater, container, false)
        val banner = lst[position]
        if(banner.show != null) {
            val show = banner.show!!
            binding.name.text = show.artistArtisticName
            binding.nameShow.text = show.name
            binding.date.text = Helper.getDateShow(show.date)
            binding.hour.text = "${Helper.getHour(show.date)} - ${Helper.getHour(show.dateFinish)}"
            binding.price.text = show.ticketValue.toString()


            Picasso.get().load(show.verticalImage).resize(1000, 0).into(binding.image)
            Picasso.get().load(show.ageRatingObj.image).into(binding.classification)
        } else if(banner.image != null) {
            Picasso.get().load(banner.image).resize(1000, 0).into(binding.image)
            binding.name.text = ""
            binding.nameShow.text = ""
            binding.date.text = ""
            binding.hour.text = ""
            binding.price.text = ""
            binding.classification.setImageDrawable(null)
        }

        binding.root.setOnClickListener { onItemClickedListener(banner) }
        binding.buy.setOnClickListener { onButtonClickedListener(banner) }

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
       // super.destroyItem(container, position, `object`)
    }
}