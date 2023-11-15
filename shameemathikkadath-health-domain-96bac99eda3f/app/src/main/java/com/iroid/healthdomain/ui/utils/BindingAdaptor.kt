package com.iroid.healthdomain.ui.utils

import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.Exception

lateinit var userPreferences: UserPreferences

@BindingAdapter("loadCircularImageProfile")
fun loadCircularImageProfile(view: ImageView, imageUrl: String?) {
    Glide.with(view.context).load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .circleCrop()
        .placeholder(R.drawable.placehold)
        .into(view)

}
@BindingAdapter("loadCircularImage")
fun loadCircularImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context).load(imageUrl)
        .circleCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.placehold)
        .into(view)
}

@BindingAdapter("bindServerDate")
fun bindServerDate(textView: TextView, date: String?) {
    textView.text = date?.let { DateUtils.getDateStringToAnotherFormat(it) }
}

@BindingAdapter("setChallengeStatus")
fun setChallengeStatus(textView: TextView, id: Int?) {
    try {
        var currentUserId: Int = 0
        userPreferences = UserPreferences(textView.context)
        currentUserId = runBlocking { userPreferences.userId.first()!! }
        Log.i("TAG", "setChallengeStatus: $currentUserId")
        if (currentUserId == id) {
            textView.text = "You Won"
        } else {
            textView.text = "You Lost"
        }
    }catch (ex:java.lang.Exception){
        ex.printStackTrace()
    }
}

@BindingAdapter("setChallengeStatusIcon")
fun setChallengeStatusIcon(view: ImageView, id: Int?) {
    try {
        var currentUserId: Int = 0
        userPreferences = UserPreferences(view.context)
        currentUserId = runBlocking { userPreferences.userId.first()!! }
        Log.i("TAG", "setChallengeStatus: $currentUserId")
        if (currentUserId == id) {
            view.setBackgroundResource(R.drawable.ic_like)
        } else {
            view.setBackgroundResource(R.drawable.ic_dislike)
        }
    }catch (ex:java.lang.Exception){
        ex.printStackTrace()
    }
}

@BindingAdapter("setNotificationStatus")
fun setNotificationStatus(btn: Button, currentStatus: String?) {
    try {
        var currentUserId: Int = 0
        userPreferences = UserPreferences(btn.context)
        currentUserId = runBlocking { userPreferences.userId.first()!! }
        Log.i("TAG", "setChallengeStatus: $currentUserId")
        when (currentStatus) {
            "PENDING" -> {
                btn.text = currentStatus
                btn.setBackgroundColor(ContextCompat.getColor(btn.context, R.color.challenge_text_color))
            }
            "ACCEPTED_LATER" -> {
                btn.text = "Pending"
                btn.setBackgroundColor(ContextCompat.getColor(btn.context, R.color.challenge_text_color))
            }
            "COMPLETED" -> {
                btn.text = currentStatus
                btn.setBackgroundColor(ContextCompat.getColor(btn.context, R.color.splash_green))
            }
            "DECLINED" -> {
                btn.text = currentStatus
                btn.setBackgroundColor(ContextCompat.getColor(btn.context, R.color.color_btn_red))
            }
            "EXPIRED" -> {
                btn.text = currentStatus
                btn.setBackgroundColor(ContextCompat.getColor(btn.context, R.color.color_btn_red))
            }
        }
    }catch (ex:java.lang.Exception){
        ex.printStackTrace()
    }
}

@BindingAdapter("setPastChallengeStatus")
fun setPastChallengeStatus(btn: Button, status: String?) {
    when (status) {
        "WON" -> {
            btn.setBackgroundColor(ContextCompat.getColor(btn.context,R.color.challenge_text_color))
        }
        "DECLINED" -> {
            btn.setBackgroundColor(ContextCompat.getColor(btn.context, R.color.challenge_text_color))
        }
        else -> {
            btn.setBackgroundColor(ContextCompat.getColor(btn.context, R.color.challenge_text_color))
        }
    }
}


