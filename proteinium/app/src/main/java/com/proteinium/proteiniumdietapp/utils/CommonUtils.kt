package com.proteinium.proteiniumdietapp.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.pojo.dislike_tags.Tag
import java.util.*


class CommonUtils {
    companion object {


        fun changeLanguageAwareContext(context: Context, lan: String) {
            val res = context.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.setLocale(Locale(lan,"US"))
            context.createConfigurationContext(conf)


        }

        fun replaceArabicNumbers(original:String): String {
            return original.replace("٠","0")
                .replace("١","1")
                .replace("٢","2")
                .replace("٣","3")
                .replace("٤","4")
                .replace("٥","5")
                .replace("٦","6")
                .replace("٧","7")
                .replace("٨","8")
                .replace("٩","9")
                .replace("٫",".")

        }


        fun setImage(context: Context, image: String, view: ImageView) {
            Glide.with(context).load(image)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view)
        }

        fun ArabicToEnglish(str: String, context: Context): String {
            var result = ""
            var en = ""
            when (str) {
                "Sun" -> en = context.getString(R.string.sun)
                "Mon" -> en = context.getString(R.string.mon)
                "Tue" -> en = context.getString(R.string.tue)
                "Wed" -> en = context.getString(R.string.wed)
                "Thu" -> en = context.getString(R.string.thu)
                "Fri" -> en = context.getString(R.string.fri)
                "Sat" -> en = context.getString(R.string.sat)

            }
            return en


        }
        fun NonStopDays(str: String, context: Context): String {
            var result = ""
            var en = ""
            when (str) {
                "Sun" -> en = context.getString(R.string.sun1)
                "Mon" -> en = context.getString(R.string.mon2)
                "Tue" -> en = context.getString(R.string.tue3)
                "Wed" -> en = context.getString(R.string.wed4)
                "Thu" -> en = context.getString(R.string.thu5)
                "Fri" -> en = context.getString(R.string.fri6)
                "Sat" -> en = context.getString(R.string.sat7)

            }
            return en
        }
        fun NonStopDays2(str: String, context: Context): String {
            var result = ""
            var en = ""
            when (str) {
                "Sun" -> en = context.getString(R.string.sun2)
                "Mon" -> en = context.getString(R.string.mon21)
                "Tue" -> en = context.getString(R.string.tue2)
                "Wed" -> en = context.getString(R.string.wed2)
                "Thu" -> en = context.getString(R.string.thu2)
                "Fri" -> en = context.getString(R.string.fri2)
                "Sat" -> en = context.getString(R.string.sat2)

            }
            return en
        }

        fun countNumber(itemList: List<Tag>): Int {
            var count = 0
            for (i in itemList) {
                if (i.disliked) {
                    count++
                }
            }
            return count
        }

        fun warningToast(context: Context, message: String) {
            val dialog = context?.let { it1 -> Dialog(it1) }
            dialog?.setCancelable(true)
            dialog?.setContentView(R.layout.alert_remove_an_item)

            val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
            val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView
            val tvContent = dialog?.findViewById(R.id.tvContent) as TextView
            tvContent.text = message

            yesBtn.setOnClickListener {
                dialog.dismiss()
            }
            closeBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog?.show()
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog?.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog?.window?.attributes = layoutParams
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        fun Activity.dismissKeyboard() {
            val inputMethodManager = getSystemService( Context.INPUT_METHOD_SERVICE ) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)


        }

    }
}
