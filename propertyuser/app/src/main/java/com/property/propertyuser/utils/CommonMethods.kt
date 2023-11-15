package com.property.propertyuser.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.property.propertyuser.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLEncoder
import java.util.*

object CommonMethods {
    var mLastClickTime=0L

    fun isOpenRecently():Boolean{
        Log.e("isOpenRecently","inside")
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }
     fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }
    fun setImage(context: Context, image: String, view: ImageView){
        Glide.with(context).load(image)
            .placeholder(R.drawable.spring_dot_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }
    fun getImageRealPath(uri : Uri?, context : Context) : String? {
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
    fun getAddress(lat : String , lng : String , context : Context) : String {
        val geocoder = Geocoder(context)
        val list = geocoder.getFromLocation(lat.toDouble() , lng.toDouble() , 1)
        return list[0].getAddressLine(0)
        /*addresses = geocoder.getFromLocation(lat, lon, 1)

    val address = addresses[0].getAddressLine(0)
    val address2 = addresses[0].getAddressLine(1)
    val city = addresses[0].locality
    val state = addresses[0].adminArea
    val country = addresses[0].countryName
    val postalCode = addresses[0].postalCode
    val knownName = addresses[0].featureName*/
    }
    fun LoadImageFromWebOperations(url: String?): Drawable? {
        return try {
            val `is`: InputStream = URL(url).content as InputStream
            Drawable.createFromStream(`is`, "src name")
        } catch (e: java.lang.Exception) {
            println("Exc=$e")
            null
        }
    }
    fun getLocalBitmapUriShare(imageView: Drawable, context: Context):Uri {
        // Extract Bitmap from ImageView drawable
        val drawable = imageView/*.drawable*/
        var bmp: Bitmap? = null
        var bmpUri: Uri? = null
        if (drawable is BitmapDrawable) {
            bmp =   (imageView/*.drawable*/ as BitmapDrawable).bitmap
            bmpUri=getBitmapFromDrawable(bmp,context)
        }
        return bmpUri!!
    }
    fun getLocalBitmapUri(imageView: ImageView,context: Context):Uri {
        // Extract Bitmap from ImageView drawable
        val drawable = imageView.drawable
        var bmp: Bitmap? = null
        var bmpUri: Uri? = null
        if (drawable is BitmapDrawable) {
            bmp =   (imageView.drawable as BitmapDrawable).bitmap
            bmpUri=getBitmapFromDrawable(bmp,context)
        }
        return bmpUri!!
    }

    // Method when launching drawable within Glide
    fun getBitmapFromDrawable(bmp: Bitmap,context: Context): Uri? {

        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {
            // Use methods on Context to access package-specific directories on external storage.
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath, "share_image_" + System.currentTimeMillis() + ".png")
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()

            // wrap File object into a content provider. NOTE: authority here
            // should match authority in manifest declaration
            bmpUri = FileProvider.getUriForFile(
                context,
                "com.codepath.fileprovider.user",
                file
            ) // use this version for API >= 24
            //bmpUri=Uri.fromFile(file)

            // **Note:** For API < 24, you may use bmpUri = Uri.fromFile(file);
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }
    fun openWhatsAppEnquiry(
        mContext : Context ,
        phone : String ,
        message : String ,
    ) {
        val smsNumber = "+91$phone"
        val isWhatsappInstalled = whatsappInstalledOrNot(mContext , "com.whatsapp")
        if (isWhatsappInstalled) {
            val sharedata = """
            $message
            
            """.trimIndent()
            val packageManager = mContext.packageManager
            val i = Intent(Intent.ACTION_VIEW)
            try {
                val url =
                    "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + URLEncoder.encode(
                        sharedata ,
                        "UTF-8")
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    mContext.startActivity(i)
                }
            } catch (e : java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            val uri = Uri.parse("market://details?id=com.whatsapp")
            val goToMarket = Intent(Intent.ACTION_VIEW , uri)
            Toast.makeText(mContext , "WhatsApp not Installed" ,
                Toast.LENGTH_SHORT).show()
            mContext.startActivity(goToMarket)
        }
    }

    fun whatsappInstalledOrNot(mContext : Context , uri : String?) : Boolean {
        val pm = mContext.packageManager
        var app_installed = false
        app_installed = try {
            pm.getPackageInfo(uri!! , PackageManager.GET_ACTIVITIES)
            true
        } catch (e : PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }

    fun changeLanguageAwareContext(context: Context, lan: String) {
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(lan))
        res.updateConfiguration(conf, dm)

    }
}