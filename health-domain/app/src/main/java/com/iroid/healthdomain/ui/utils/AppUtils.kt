package com.iroid.healthdomain.ui.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*


fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}


fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    val view: View = snackbar.view

    try {
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        view.layoutParams = params
        snackbar.show()
    } catch (e: Exception) {
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        view.layoutParams = params
        snackbar.show()
    }
}


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun View.showSnackBar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    snackbar.show()
}

fun RecyclerView.disableItemAnimator() {
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}

fun Fragment.handleApiError(
    view: View = requireView(),
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetWorkError -> requireView().snackbar(
            "No internet connection",
            retry
        )
        failure.errorCode == 401 -> {
            if (this is BaseFragment<*, *, *>) {
                view.snackbar("Server error 404")
            } else {

            }
        }
        failure.errorCode == 500 -> {
            if (this is BaseFragment<*, *, *>) {
                view.snackbar("Internal server error")
            } else {

            }
        }
        failure.errorCode == 422 -> {
            if (this is BaseFragment<*, *, *>) {
                val message: String? = failure.errorBody!!.string().substringAfterLast("message")
                val extractedMessage = message?.subSequence(3, message.length-2)
                view.snackbar(extractedMessage.toString())
            } else {

            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }


}