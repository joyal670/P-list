package com.iroid.patrickstore.ui.coupon.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.ItemCouponBinding
import com.iroid.patrickstore.model.coupon.ItemCoupon

class CouponAdapter(
    private val context: Context,
    private val couponList: List<ItemCoupon>,
    private val function: (String) -> Unit
) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    private val bgList = listOf<Int>(
        R.drawable.ic_subtraction_1,
        R.drawable.ic_subtraction_2,
        R.drawable.ic_subtraction_3,
        R.drawable.ic_subtraction_1,
        R.drawable.ic_subtraction_2,
        R.drawable.ic_subtraction_3
    )

    class CouponViewHolder(var binding: ItemCouponBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(itemCoupon: ItemCoupon) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvDec.text = Html.fromHtml("<p><strong>${itemCoupon.percentage}% OFF</strong></p>\n" +
                        "<p>${itemCoupon.name}: Get ${itemCoupon.percentage}% OFF On use code ${itemCoupon.code}</p>", Html.FROM_HTML_MODE_COMPACT);
            } else {
                binding.tvDec.text = Html.fromHtml("<p><strong>${itemCoupon.percentage}% OFF</strong></p>\\n\" +\n" +
                        "\"<p>${itemCoupon.name}: Get ${itemCoupon.percentage}% OFF On use code ${itemCoupon.code}</p>\"");
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val binding = ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {

        holder.binding.imageView7.setImageResource(bgList[position])
        holder.bindItem(couponList[position])
        holder.binding.btnRedeem.setOnClickListener {
            function.invoke(couponList[position].code)
        }

    }
}