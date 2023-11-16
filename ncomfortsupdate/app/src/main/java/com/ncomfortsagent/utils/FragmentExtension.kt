package com.ncomfortsagent.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.ncomfortsagent.R


fun AppCompatActivity.replaceFragment(
    frameId: Int = R.id.main_container,
    fragment: Fragment,
    addToBackStack: Boolean = false
) {
    supportFragmentManager.doTransaction {
        if (addToBackStack)
            addToBackStack(null)
        replace(frameId, fragment)
    }
}

inline fun FragmentManager.doTransaction(
    func: FragmentTransaction.() ->
    FragmentTransaction
) {
    beginTransaction().func().commit()
}

inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.forEachVisibleHolder(
    action: (T) -> Unit
) {
    for (i in 0 until childCount) {
        action(getChildViewHolder(getChildAt(i)) as T)
    }
}

fun Context.changeStatusBarColor(window: Window, red: Int, green: Int, blue: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = Color.rgb(red, green, blue)
    }
}