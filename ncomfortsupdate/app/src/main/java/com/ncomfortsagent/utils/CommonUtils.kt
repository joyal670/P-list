package com.ncomfortsagent.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidadvance.topsnackbar.TSnackbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.aviran.cookiebar2.CookieBar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class CommonUtils {
    companion object {
        var mLastClickTime = 0L
        fun isOpenRecently(): Boolean {
            Log.e("isOpenRecently", "inside")
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return true
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            return false
        }

        fun showWarningTopSnackBar(
            view: AppCompatActivity,
            message: String?,
            hasStatusBar: Boolean
        ) {
            try {
                val tSnackbar = TSnackbar.make(
                    view.findViewById(R.id.content),
                    message!!,
                    TSnackbar.LENGTH_LONG
                )
                val snackBarView = tSnackbar.view
                if (!hasStatusBar) {
                    var statusBar = 0
                    val resourceId =
                        view.resources.getIdentifier("status_bar_height", "dimen", "android")
                    if (resourceId > 0) {
                        statusBar = view.resources.getDimensionPixelSize(resourceId)
                    }
                    snackBarView.setPadding(0, statusBar, 0, 0)
                }
                snackBarView.setBackgroundColor(
                    Color.RED
                )
//                tSnackbar.setIconLeft(R.drawable.ic_warning_white_32dp, 24f)
                tSnackbar.setIconPadding(10)
                val textView =
                    snackBarView.findViewById<TextView>(com.androidadvance.topsnackbar.R.id.snackbar_text)
                textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(R.dimen.notification_large_icon_height)
                )
                textView.setTextColor(Color.WHITE)
                tSnackbar.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getImageRealPath(uri: Uri?, context: Context): String? {
            try {
                val imagePath: String
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context.contentResolver.query(
                    uri!!,
                    filePathColumn,
                    null,
                    null,
                    null
                )
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imagePath = cursor.getString(columnIndex)
                cursor.close()
                return imagePath
            } catch (ex: Exception) {
                Log.e("123456", "getImageRealPath: " + ex)
                return ""
            }

        }

        fun Neq(
            uri: Uri?,
            context: Context
        ): String? {
            val imagePath: String
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(
                uri!!,
                filePathColumn,
                null,
                null,
                null
            )
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            imagePath = cursor.getString(columnIndex)
            cursor.close()
            return imagePath
        }


        fun showCookieBar(context: Activity, title: String, msg: String, bgColor: Int) {
            CookieBar.build(context)
                .setTitle(title)
                .setMessage(msg)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(bgColor)
                .setTitleColor(com.ncomfortsagent.R.color.white)
                .setAnimationIn(
                    R.anim.slide_in_left,
                    R.anim.slide_in_left
                )
                .setAnimationOut(
                    R.anim.slide_out_right,
                    R.anim.slide_out_right
                )
                .setEnableAutoDismiss(true)
                .setSwipeToDismiss(true)
                .setDuration(3000)
                .show()
        }

        fun shareProperty(message: String, context: Context, imageUrl: String?) {
            Picasso.get().load(imageUrl).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    var fileUri: String? = null
                    var imgUri = Uri.EMPTY
                    try {
                        val cachePath = File(context.cacheDir, "images")
                        if (!cachePath.exists()) {
                            cachePath.mkdirs()
                        }
                        fileUri =
                            cachePath.absolutePath + File.separator + System.currentTimeMillis() + ".jpg"
                        val outputStream = FileOutputStream(fileUri)
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.flush()
                        outputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    try {
                        imgUri =
                            Uri.parse(
                                MediaStore.Images.Media.insertImage(
                                    context.contentResolver,
                                    BitmapFactory.decodeFile(fileUri),
                                    "IMG_" + Calendar.getInstance().time,
                                    null
                                )
                            )
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }

                    try {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, message)
                            putExtra(Intent.EXTRA_STREAM, imgUri)
                            type = "image/*"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    Toast.makeText(context, "Wait for loading...", Toast.LENGTH_SHORT).show()
                }

            })
        }

        fun shareBankDetails(message: String, context: Context) {
            try {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, message)
                    type = "text/*"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun changeLanguageAwareContext(context: Context, lan: String) {
            val res = context.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.setLocale(Locale(lan))
            res.updateConfiguration(conf, dm)

        }

        fun Activity.dismissKeyboard() {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isAcceptingText)
                inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        }

    }
}