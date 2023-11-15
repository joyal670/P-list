package com.proteinium.proteiniumdietapp.utils


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R

fun AppCompatActivity.replaceFragment(
    frameId: Int = R.id.main_container,
    fragment: Fragment,
    addToBackStack: Boolean = false
) {
    supportFragmentManager.doTransaction {
        setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
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