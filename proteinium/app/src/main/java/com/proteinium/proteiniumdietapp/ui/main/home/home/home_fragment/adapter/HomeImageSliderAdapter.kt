package com.proteinium.proteiniumdietapp.ui.main.home.home.home_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.home.Banner
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import kotlinx.android.synthetic.main.list_home_image_slider_item.view.*


class HomeImageSliderAdapter(private val context: Context,
                             private val bannerImageList: List<Banner>
) : RecyclerView.Adapter<HomeImageSliderAdapter.ImageHolder>() {


    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_home_image_slider_item, parent, false)
        return ImageHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return bannerImageList.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        /*holder.itemView.ivHomeSlideItem.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                bannerImageList[position].image
            )
        )*/
        CommonUtils.setImage(context,bannerImageList[position].image,holder.itemView.ivHomeSlideItem)

    }
}