package com.property.propertyuser.ui.main.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_list.Document
import com.property.propertyuser.modal.property_list.Event
import com.property.propertyuser.modal.property_list.Property
import com.property.propertyuser.modal.testing.HomePropertyEventModel
import com.property.propertyuser.ui.main.home.events_see_all.EventsSeeAllActivity
import com.property.propertyuser.ui.main.property_details.slide_images.PosterOverlayView
import com.property.propertyuser.utils.CommonMethods
import com.property.propertyuser.utils.loadImagesWithGlideExtFull
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.event_recycler_view.view.*
import kotlinx.android.synthetic.main.list_home_property_item.view.*


class TestMainRecyclerViewAdapter(
    private val context: Context,
    private val homePropertyEventModelList: ArrayList<HomePropertyEventModel>,
    private val functionInfo: (Int, String) -> Unit,
    private val functionMap: (Int) -> Unit, private val functionBook: (Int) -> Unit,
    private val functionAddToFav: (Int, Int) -> Unit,
    private val functionRemoveFromFav: (Int, Int) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var imageSliderList = ArrayList<String>()

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
        private const val VIEW_TYPE_PROGRESS = 3
    }

    private var lastPosition = -1
    private var overlayView: PosterOverlayView? = null
    lateinit var builder: StfalconImageViewer<String>
    val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)

    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(
            position: Int,
            property: Property
        ) {
            itemView.tvPropertyName.text = property.property_name
            if (property.documents.isNotEmpty()) {
                CommonMethods.setImage(
                    context,
                    property.documents[0].document,
                    itemView.ivPropertyImage
                )
            }
            if (property.property_to == 0) {
                itemView.tvAppartmentText.text = context.getString(R.string.property_for_rent)
                itemView.tvPropertyAmount.text = "SAR " + property.rent
                itemView.tvPropertyDiscountAmount.visibility = View.GONE
            } else {
                itemView.tvAppartmentText.text = context.getString(R.string.property_for_sale)
                itemView.tvPropertyDiscountAmount.visibility = View.VISIBLE
                itemView.tvPropertyAmount.text = "SAR " + property.selling_price
                itemView.tvPropertyDiscountAmount.text = "SAR " + property.mrp
            }
            itemView.tvPropertyDiscountAmount.paintFlags =
                itemView.tvPropertyDiscountAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            itemView.tvRating.text = property.rating
            itemView.tvBedCount.text = property.Beds.toString()
            itemView.tvBathCount.text = property.Bathroom.toString()
            itemView.tvDiameter.text = property.area.toString() + "sq. M."
            //itemView.iconFavourite.isCheckable = propertyData.is_favourite==1
            if (property.is_favourite == 1) {
                itemView.iconFavourite.visibility = View.GONE
                itemView.iconFavouriteSelected.visibility = View.VISIBLE
            } else {
                itemView.iconFavouriteSelected.visibility = View.GONE
                itemView.iconFavourite.visibility = View.VISIBLE
            }
            if (property.is_featured == 1) {
                itemView.ivGreenTag.visibility = View.VISIBLE
            } else {
                itemView.ivGreenTag.visibility = View.GONE
            }
            itemView.iconFavourite.setOnClickListener {
                itemView.iconFavourite.visibility = View.GONE
                itemView.iconFavouriteSelected.visibility = View.VISIBLE
                itemView.iconFavouriteSelected.startAnimation(animation)
                itemView.wishListAnimation.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    itemView.wishListAnimation.visibility = View.GONE
                    itemView.iconFavouriteSelected.clearAnimation()
                }, 2000)

                property.is_favourite = 1
                functionAddToFav.invoke(property.id, position)
            }
            itemView.iconFavouriteSelected.setOnClickListener {
                itemView.iconFavouriteSelected.visibility = View.GONE
                itemView.iconFavourite.visibility = View.VISIBLE
                itemView.iconFavourite.startAnimation(animation)
                //itemView.wishListAnimation.visibility = View.VISIBLE
                property.is_favourite = 0
                Handler(Looper.getMainLooper()).postDelayed({
                    //itemView.wishListAnimation.visibility = View.GONE
                    itemView.iconFavourite.clearAnimation()
                }, 2000)

                functionRemoveFromFav.invoke(property.id, position)
            }
            itemView.ivInfo.setOnClickListener {
                functionInfo.invoke(property.id, "")
            }
            itemView.ivMap.setOnClickListener {
                functionMap.invoke(property.id)
            }
            itemView.ivCalender.setOnClickListener {
                functionBook.invoke(property.id)
            }
            itemView.ivShare.setOnClickListener {
                //shareFile()
                val stringBuilder = StringBuilder()
                stringBuilder.append("Property Name :" + property.property_name)
                stringBuilder.append("\nProperty Type" + itemView.tvAppartmentText.text)
                stringBuilder.append("\nhttps://siaaha.com/property/${property.id}")
                Log.e("te", stringBuilder.toString())
                shareTest(stringBuilder.toString(), itemView.ivPropertyImage, context)
            }
            itemView.linearTransparent.setOnClickListener {
                //functionInfo.invoke(property.id,"")
                /*if(property.documents.isNotEmpty()){
                    StfalconImageViewer.Builder<Document>(context,property.documents, ::loadPosterImage)
                        .show()
                }*/
                /*val intent=Intent(context, PropertyImageViewActivity::class.java)
                intent.putParcelableArrayListExtra("documentImageList",property.documents as ArrayList<out Parcelable> )
                context.startActivity(intent)*/
                imageSliderList.clear()
                for (i in property.documents.indices) {
                    if (property.documents[i].type == 0) {
                        imageSliderList.add(property.documents[i].document)
                    }
                }
                setOverlay()
                builder = StfalconImageViewer.Builder(context, imageSliderList) { view, image ->
                    view.background =
                        ContextCompat.getDrawable(context, R.drawable.shape_placeholder)
                    Glide.with(context)
                        .load(image)
                        .error(R.drawable.shape_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view)
                }.withOverlayView(overlayView).withHiddenStatusBar(false).show()
            }

            val animation: Animation = AnimationUtils.loadAnimation(
                context,
                if (position > lastPosition) R.anim.up_from_bottom else R.anim.down_from_top
            )
            itemView.startAnimation(animation)
            lastPosition = position
        }
    }

    private fun setOverlay() {
        overlayView = PosterOverlayView(context).apply {
            update()
            onClose = {
                builder.close()
            }
        }
    }

    private fun loadPosterImage(imageView: ImageView, document: Document?) {
        imageView.apply {
            background = context.getDrawable(R.drawable.shape_placeholder)
            loadImagesWithGlideExtFull(document!!.document)
        }
    }

    private fun shareTest(
        content: String,
        ivPropertyImage: ImageView,
        context: Context
    ) {

        Log.e("eee", CommonMethods.getLocalBitmapUri(ivPropertyImage, context).toString())
        val uriImage = CommonMethods.getLocalBitmapUri(ivPropertyImage, context)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_STREAM, uriImage)
            type = "image/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        this.context.startActivity(shareIntent)
    }

    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(
            position: Int,
            events: List<Event>
        ) {
            itemView.tvEventsSeeAll.setOnClickListener {
                context.startActivity(Intent(context, EventsSeeAllActivity::class.java))
            }
            itemView.rvEventsList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemView.rvEventsList.adapter = HomeEventItemAdapter(context, events)
        }
    }

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_ONE -> {
                return View1ViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.list_home_property_item, parent, false)
                )
            }
            VIEW_TYPE_TWO -> {
                return View2ViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.event_recycler_view, parent, false)
                )
            }
            VIEW_TYPE_PROGRESS -> {
                return ProgressViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.loader, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemCount(): Int {
        return homePropertyEventModelList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (homePropertyEventModelList[position].viewType === VIEW_TYPE_ONE) {
            (holder as View1ViewHolder).bind(
                position,
                homePropertyEventModelList[position].property_data
            )
        } else if (homePropertyEventModelList[position].viewType === VIEW_TYPE_TWO) {
            if (!(homePropertyEventModelList[position].events.isNullOrEmpty())) {
                holder.itemView.constraintMainEvent.visibility = View.VISIBLE
                (holder as View2ViewHolder).bind(
                    position,
                    homePropertyEventModelList[position].events
                )
            } else {
                holder.itemView.constraintMainEvent.visibility = View.GONE
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return homePropertyEventModelList[position].viewType
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}