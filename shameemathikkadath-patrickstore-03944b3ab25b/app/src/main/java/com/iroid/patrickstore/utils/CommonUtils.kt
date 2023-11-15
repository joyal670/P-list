package com.iroid.patrickstore.utils

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.location.Address
import android.location.Geocoder
import android.media.browse.MediaBrowser
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.iroid.patrickstore.R
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class CommonUtils {
    companion object {
        fun setImageBase(context: Context, image: String, view: ImageView) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 10f
            circularProgressDrawable.centerRadius = 50f
            circularProgressDrawable.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            )
            circularProgressDrawable.start()
            Glide.with(context).load(image)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view)
        }
        fun setImage(context: Context, image: String, view: ImageView){
            Glide.with(context).load(image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view)
        }

        fun setImageFitCenter(context: Context, image: String, view: ImageView) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 10f
            circularProgressDrawable.centerRadius = 50f
            circularProgressDrawable.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                ), PorterDuff.Mode.SRC_IN
            )
            circularProgressDrawable.start()
            Glide.with(context).load(image)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.placeholder)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view)
        }

        fun setToolbarMargin(context: Context, view: View) {
            val tv = TypedValue()
            if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                val actionBarHeight =
                    TypedValue.complexToDimensionPixelSize(
                        tv.data,
                        context.resources.displayMetrics
                    )
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(0, 0, actionBarHeight, 0)
                }
            }
        }

        fun getCurrencyFormat(amount:String): String? {
            val format: NumberFormat = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("INR")

            return format.format(amount.toString().toDouble())
        }

         fun getCityNameByCoordinates(context:Context, lat: Double, lon: Double): String? {
            val mGeocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> = mGeocoder.getFromLocation(lat, lon, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                println("HAHAHA place details: country " + addresses[0].countryName + " PostalCode " + addresses[0].postalCode + " getLocality " + addresses[0].locality + " getLocale " + addresses[0].locale + " getSubLocality " + addresses[0].adminArea)
                return addresses[0].locality
            }
            return null
        }

        fun noInternet(lifecycle:Lifecycle,context: Context){
            NoInternetDialogSignal.Builder(
                context as Activity,
                lifecycle
            ).apply {
                dialogProperties.apply {
                    connectionCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    object : MediaBrowser.ConnectionCallback(), ConnectionCallback {

                        override fun hasActiveConnection(hasActiveConnection: Boolean) {

                        }
                    }
                    cancelable = false // Optional
                    noInternetConnectionTitle = "No Internet"
                    noInternetConnectionMessage =
                        "Check your Internet connection and try again."
                    showInternetOnButtons = true
                    pleaseTurnOnText = "Please turn on"
                    wifiOnButtonText = "Wifi"
                    mobileDataOnButtonText = "Mobile data"

                    onAirplaneModeTitle = "No Internet"
                    onAirplaneModeMessage = "You have turned on the airplane mode."
                    pleaseTurnOffText = "Please turn off"
                    airplaneModeOffButtonText = "Airplane mode"
                    showAirplaneModeOffButtons = true
                }
            }.build()

            // No Internet Snackbar: Fire


    }
        }
    }


