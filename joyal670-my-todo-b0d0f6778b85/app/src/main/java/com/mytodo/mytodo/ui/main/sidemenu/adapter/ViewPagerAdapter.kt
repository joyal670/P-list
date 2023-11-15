package com.mytodo.mytodo.ui.main.sidemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.mytodo.mytodo.R
import java.util.*


class ViewPagerAdapter(context: Context, images: IntArray) : PagerAdapter() {

    var context: Context
    var images: IntArray

    var mLayoutInflater: LayoutInflater

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(@NonNull view: View, @NonNull `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView: View = mLayoutInflater.inflate(R.layout.image_slider, container, false)

        // referencing the image view from the item.xml file
        val imageView: ImageView = itemView.findViewById(R.id.imageViewMain) as ImageView

        // setting the image in the imageView
        imageView.setImageResource(images[position])

        // Adding the View
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    // Viewpager Constructor
    init {
        this.context = context
        this.images = images
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}