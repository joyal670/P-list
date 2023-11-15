package com.property.propertyuser.ui.main.property_details.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.modal.property_details.FloorPlan
import com.property.propertyuser.ui.main.property_details.slide_images.PosterOverlayView
import com.property.propertyuser.utils.loadImagesWithGlideExtFull
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.list_property_floor_image_item.view.*


class PropertyFloorPlanImageSlider2Adapter(
    private val context: Context,
    private val floorPlansList: List<FloorPlan>
) : RecyclerView.Adapter<PropertyFloorPlanImageSlider2Adapter.FloorPlansHolder>() {

    private var overlayView: PosterOverlayView? = null
    lateinit var builder: StfalconImageViewer<FloorPlan>

    class FloorPlansHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloorPlansHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_property_floor_image_item, parent, false)
        return FloorPlansHolder(v)
    }

    override fun getItemCount(): Int {
        return floorPlansList.size
    }

    override fun onBindViewHolder(holder: FloorPlansHolder, position: Int) {

        holder.itemView.ivImageFloor.loadImagesWithGlideExtFull(floorPlansList[position].document)
        holder.itemView.ivImageFloor.setOnClickListener {
            setOverlay()
            builder =
                StfalconImageViewer.Builder<FloorPlan>(context, floorPlansList) { view, image ->
                    Log.e("check", Gson().toJson(image))
                    view.background =
                        ContextCompat.getDrawable(context, R.drawable.shape_placeholder)
                    Glide.with(context)
                        .load(image.document)
                        .error(R.drawable.shape_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view)
                }.withOverlayView(overlayView).withHiddenStatusBar(false).show()
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

}