package com.mytodo.mytodo.utils

import android.R
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidadvance.topsnackbar.TSnackbar


class CommonUtils {
    companion object {
        fun showWarningTopSnackBar(
            view : AppCompatActivity ,
            message : String? ,
            hasStatusBar : Boolean
        ) {
            try {
                val tSnackbar = TSnackbar.make(
                    view.findViewById(R.id.content) ,
                    message!! ,
                    TSnackbar.LENGTH_LONG
                )
                val snackBarView = tSnackbar.view
                if (!hasStatusBar) {
                    var statusBar = 0
                    val resourceId =
                        view.resources.getIdentifier("status_bar_height" , "dimen" , "android")
                    if (resourceId > 0) {
                        statusBar = view.resources.getDimensionPixelSize(resourceId)
                    }
                    snackBarView.setPadding(0 , statusBar , 0 , 0)
                }
                snackBarView.setBackgroundColor(
                    Color.RED
                )
//                tSnackbar.setIconLeft(R.drawable.ic_warning_white_32dp, 24f)
                tSnackbar.setIconPadding(10)
                val textView =
                    snackBarView.findViewById<TextView>(com.androidadvance.topsnackbar.R.id.snackbar_text)
                textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX ,
                    view.resources.getDimension(R.dimen.notification_large_icon_height)
                )
                textView.setTextColor(Color.WHITE)
                tSnackbar.show()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }

        fun getImageRealPath(uri : Uri? , context : Context) : String? {
            try {
                val imagePath : String
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context.contentResolver.query(
                    uri!! ,
                    filePathColumn ,
                    null ,
                    null ,
                    null
                )
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imagePath = cursor.getString(columnIndex)
                cursor.close()
                return imagePath
            } catch (ex : Exception) {
                Log.e("123456" , "getImageRealPath: " + ex)
                return ""
            }

        }

        fun Neq(
            uri : Uri? ,
            context : Context
        ): String? {
            val imagePath: String
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(
                uri!! ,
                filePathColumn ,
                null ,
                null ,
                null
            )
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            imagePath = cursor.getString(columnIndex)
            cursor.close()
            return imagePath
        }


    }


}