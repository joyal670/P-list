package com.mytodo.mytodo.ui.main.sidemenu.activity

import android.util.Log
import androidx.fragment.app.Fragment
import com.mytodo.mytodo.R
import com.mytodo.mytodo.base.BaseActivity
import com.mytodo.mytodo.databinding.ActivityMyTaskBinding
import com.mytodo.mytodo.listeners.ActivityListener
import com.mytodo.mytodo.ui.main.sidemenu.fragments.fragment_addtask
import com.mytodo.mytodo.ui.main.sidemenu.fragments.fragment_completedTask
import com.mytodo.mytodo.utils.Constants.TASKTYPE
import com.mytodo.mytodo.utils.EnumFromPage
import com.mytodo.mytodo.utils.replaceFragment

class MyTaskActivity : BaseActivity<ActivityMyTaskBinding>(), ActivityListener
{
    override val layoutId: Int
        get() = R.layout.activity_my_task
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityMyTaskBinding =
        ActivityMyTaskBinding.inflate(layoutInflater)

    override fun initData() {
        lanuchFragment(intent.getStringExtra(TASKTYPE))
    }

    private fun lanuchFragment(stringExtra: String?) {
        when (stringExtra) {
            EnumFromPage.COMPLETED_TASK.name -> {
                replaceFragment(fragment = fragment_completedTask())
            }

            EnumFromPage.ADD_TASK.name -> {
                replaceFragment(fragment = fragment_addtask())
            }


        }

    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

    override fun navigateToFragment(fragment: Fragment) {
        replaceFragment(fragment = fragment, addToBackStack = true)
    }

}