package com.iroid.patrickstore.ui.productdetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.model.single_product.ImgUrl
import com.iroid.patrickstore.utils.CommonUtils
import kotlinx.android.synthetic.main.item_preview.view.*

class PreviewAdapter(private val context:Context, private val imgUrl: List<ImgUrl>) :
    RecyclerView.Adapter<PreviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_preview, parent, false))
    }

    override fun getItemCount(): Int {
        return imgUrl.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imgUrl[position],context)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(imgUrl: ImgUrl, context: Context) {
            CommonUtils.setImageBase(context,imgUrl.publicUrl,itemView.ivProduct)
        }
    }


}