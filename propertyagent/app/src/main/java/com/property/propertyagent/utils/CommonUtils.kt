package com.property.propertyagent.utils


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.androidadvance.topsnackbar.TSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.property.propertyagent.R
import com.property.propertyagent.utils.AppPreferences.login_type
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLEncoder
import java.util.*

class CommonUtils {
    companion object {
        fun getAddress(lat: String, lng: String, context: Context): String {
            val geocoder = Geocoder(context)
            val list = geocoder.getFromLocation(lat.toDouble(), lng.toDouble(), 1)
            return list[0].getAddressLine(0)
        }

        fun showWarningTopSnackBar(
            view: AppCompatActivity,
            message: String?,
            hasStatusBar: Boolean,
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
            context: Context,
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

        fun openWhatsAppEnquiry(
            mContext: Context,
            phone: String,
            message: String,
        ) {
            val smsNumber = "+91$phone"
            val isWhatsappInstalled = whatsappInstalledOrNot(mContext, "com.whatsapp")
            if (isWhatsappInstalled) {
                val sharedata = """
            $message
            
            """.trimIndent()
                val packageManager = mContext.packageManager
                val i = Intent(Intent.ACTION_VIEW)
                try {
                    val url =
                        "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + URLEncoder.encode(
                            sharedata,
                            "UTF-8"
                        )
                    i.setPackage("com.whatsapp")
                    i.data = Uri.parse(url)
                    if (i.resolveActivity(packageManager) != null) {
                        mContext.startActivity(i)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else {
                val uri = Uri.parse("market://details?id=com.whatsapp")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                Toast.makeText(
                    mContext, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT
                ).show()
                mContext.startActivity(goToMarket)
            }
        }

        fun whatsappInstalledOrNot(mContext: Context, uri: String?): Boolean {
            val pm = mContext.packageManager
            var app_installed = false
            app_installed = try {
                pm.getPackageInfo(uri!!, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
            return app_installed
        }

        fun getShortAddress(lat: Double, lng: Double, context: Context): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)
            val obj: Address = addresses[0]

            return obj.subAdminArea
        }

        fun dailPhone(mContext: Context, phone: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phone")
            mContext.startActivity(intent)
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
                    } catch (e: java.lang.Exception) {
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

        fun changeLanguageAwareContext(context: Context, lan: String) {
            val res = context.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.setLocale(Locale(lan))
            res.updateConfiguration(conf, dm)

        }

        fun TextView.underline() {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

        fun showUserProfile(
            activity: Activity,
            userType: String,
            profileName: String,
            phone: String,
            profilePic: String
        ) {
            val bottom = BottomSheetDialog(activity, R.style.ThemeOverlay_App_BottomSheetDialog)
            val bottomSheet: View =
                activity.layoutInflater.inflate(R.layout.profile_layout_bottom_sheet, null)

            val closeBtn = bottomSheet.findViewById<ImageButton>(R.id.ivClose)
            val imageView =
                bottomSheet.findViewById<CircleImageView>(R.id.civProfilePicRequestDialog)
            val name = bottomSheet.findViewById<TextView>(R.id.tvProfileNameRequestDialog)
            val user = bottomSheet.findViewById<TextView>(R.id.tvUserTypeRequestDialog)
            val callBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivPhoneRequestDialog)
            val msgBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivWhatsappRequestDialog)

            if(login_type == "1"){
                callBtn.backgroundTintList = ContextCompat.getColorStateList(activity, R.color.color_accent_blue_statusbar)
                msgBtn.backgroundTintList = ContextCompat.getColorStateList(activity, R.color.color_accent_blue_statusbar)
            }
            closeBtn.setOnClickListener {
                bottom.dismiss()
            }

            imageView.loadImagesWithGlideExt(profilePic)
            name.text = profileName
            user.text = userType

            callBtn.setOnClickListener {
                val ph: String = phone
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$ph")
                activity.startActivity(intent)
            }

            msgBtn.setOnClickListener {
                openWhatsAppEnquiry(
                    activity,
                    phone,
                    activity.getString(R.string.hai)
                )
            }

            bottom.setContentView(bottomSheet)
            bottom.show()
        }
    }
}