package com.iroid.patrickstore.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.regex.Pattern
import javax.inject.Inject

@Suppress("DEPRECATION")
class Utils @Inject constructor(@ApplicationContext private val context: Context) {

    //check network is available or not
    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    //Characters and Numbers only (min 4) (max 30)
    fun isUsernameValid(username: String): Boolean {
        val expression = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,30}$"
        return checkExpression(username, expression)
    }

    //Characters and Numbers only (min 4) (max 16)
    fun isPasswordValid(password: String): Boolean {
        val expression = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,16}$"
        return checkExpression(password, expression)
    }

    private fun checkExpression(text: String, expression: String): Boolean {
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }
}