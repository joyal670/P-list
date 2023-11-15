package com.iroid.patrickstore.model.daily_deal

import android.os.Parcel
import android.os.Parcelable
import com.iroid.patrickstore.model.home.ImgUrl

data class ProductDailyDeal(
    val __v: Int,
    val actualPrice: Double,
    val adminCommission: Int,
    val adminCommissionAmount: Double,
    val canSubtractStock: Boolean,
    val color: String,
    val costPrice: Double,
    val description: String,
    val discountDateFrom: String,
    val discountDateTo: String,
    val franchiseId: String,
    val height: String,
    val _id: String,
    val isApproved: Boolean,
    val isEnabled: Boolean,
    val isRejected: Boolean,
    val length: String,
    val maxDiscount: Int,
    val maxQuantity: Int,
    val minQunatity: Int,
    val model: String,
    val name: String,
    val offerPrice: Double,
    val productId: Int,
    val productTypeId: String,
    val quantity: Int,
    val shopAmount: Double,
    val shopDiscount: Int,
    val size: String,
    val tag: String,
    val weight: String,
    val width: String,
    val images: List<Image>
)
