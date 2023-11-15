package com.mytodo.mytodo.ui.main.home.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mytodo.mytodo.R
import com.mytodo.mytodo.base.BaseActivity
import com.mytodo.mytodo.databinding.ActivityDashboardBinding
import com.mytodo.mytodo.listeners.ActivityListener
import com.mytodo.mytodo.notification.AlarmReceiver
import com.mytodo.mytodo.start_up.auth.activity.AuthActivity
import com.mytodo.mytodo.ui.main.sidemenu.fragments.*
import com.mytodo.mytodo.ui.main.sidemenu.realm.model.TaskModel
import com.mytodo.mytodo.utils.AppPreferences.prefIsDashboardTutorial
import com.mytodo.mytodo.utils.AppPreferences.prefIsLogin
import com.mytodo.mytodo.utils.AppPreferences.prefIsTaskTutorial
import com.mytodo.mytodo.utils.AppPreferences.prefUserDisplayName
import com.mytodo.mytodo.utils.AppPreferences.prefUserEmail
import com.mytodo.mytodo.utils.AppPreferences.prefUserID
import com.mytodo.mytodo.utils.AppPreferences.prefUserProfilePic
import com.mytodo.mytodo.utils.Toaster
import com.mytodo.mytodo.utils.replaceFragment
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer
import io.realm.Realm
import io.realm.RealmResults
import java.util.*
import kotlin.system.exitProcess


class DashboardActivity : BaseActivity<ActivityDashboardBinding>(), ActivityListener {
    var fragment: Fragment? = null
    var fragmentClass: Class<*>? = null
    private var doubleBackToExitPressedOnce = false

    private var dataModel: TaskModel? = null
    var taskList: RealmResults<TaskModel>? = null


    override val layoutId: Int
        get() = R.layout.activity_dashboard
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityDashboardBinding = ActivityDashboardBinding.inflate(
        layoutInflater
    )

    override fun initData() {
        /*  Notify.build(applicationContext)
              .setTitle("Jill Zhao")
              .setContent("Hi! So I meet you today?")
              .setSmallIcon(R.drawable.ic_dashboard)
              .setColor(R.color.colorPrimary)
              .largeCircularIcon()
              .show()*/

        scheduleAlarm()
    }

    private fun scheduleAlarm() {
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        alarmIntent.putExtra("data", "Tap to see your Task")
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val afterTwoMinutes = SystemClock.elapsedRealtime() + 1 * 1 * 1000

        /* val alaranager : AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         alaranager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), 1000 * 60, pendingIntent);
 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP, afterTwoMinutes, 1000 * 1, pendingIntent
        ) else alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            afterTwoMinutes, pendingIntent
        )

        /* alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,System.currentTimeMillis(), 1000 * 60, pendingIntent)*/

    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {
        val menuItems = ArrayList<MenuItem>()
        menuItems.add(MenuItem("Dashboard", R.drawable.ic_dashboard))
        menuItems.add(MenuItem("My Task", R.drawable.ic_task))
        menuItems.add(MenuItem("Calender", R.drawable.ic_schedule))
        menuItems.add(MenuItem("Profile", R.drawable.ic_profile_user))
        menuItems.add(MenuItem("Notifications", R.drawable.ic_chat))
        menuItems.add(MenuItem("About", R.drawable.ic_information))
        menuItems.add(MenuItem("Logout", R.drawable.ic_logout))
        binding.navigationDrawer.menuItemList = menuItems

        fragmentClass = fragment_dashboard::class.java

        binding.navigationDrawer.setAppbarTitleTV("Dashboard")
        binding.navigationDrawer.secondaryMenuItemTextSize = 1F
        binding.navigationDrawer.primaryMenuItemTextSize = 2F
        binding.navigationDrawer.menuIconSize = 0.5F

        try {
            fragment = (fragmentClass as Class<*>).newInstance() as Fragment
        } catch (ex: Exception) {
            Toast.makeText(this, "" + ex.message, Toast.LENGTH_SHORT).show()
        }

        if (fragment != null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.main_container, fragment!!).commit()
        }

        binding.navigationDrawer.drawerListener = object : SNavigationDrawer.DrawerListener {
            override fun onDrawerOpening() {
            }

            override fun onDrawerClosing() {
                try {
                    fragment = (fragmentClass as Class<*>).newInstance() as Fragment
                } catch (ex: Exception) {
                    Toast.makeText(this@DashboardActivity, "" + ex.message, Toast.LENGTH_SHORT)
                        .show()
                }
                if (fragment != null) {
                    val fragmentManager: FragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                        .setCustomAnimations(
                            android.R.animator.fade_in,
                            android.R.animator.fade_out
                        )
                        .replace(R.id.main_container, fragment!!).commit()
                }
            }

            override fun onDrawerOpened() {
            }

            override fun onDrawerClosed() {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        }
        binding.navigationDrawer.onMenuItemClickListener =
            SNavigationDrawer.OnMenuItemClickListener { position ->
                when (position) {
                    0 -> {
                        fragmentClass = fragment_dashboard::class.java
                    }
                    1 -> {
                        fragmentClass = fragment_mytask::class.java
                    }
                    2 -> {
                        fragmentClass = fragment_calender::class.java
                    }
                    3 -> {
                        fragmentClass = fragment_profile::class.java
                    }
                    4 -> {
                        fragmentClass = fragment_notification::class.java
                    }
                    5 -> {
                        fragmentClass = fragment_about::class.java
                    }
                    6 -> {

                        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        pDialog.titleText = "Are you sure?"
                        pDialog.contentText = "Won't be able to recover saved tasks!"
                        pDialog.confirmText = "Yes, Logout!"
                        pDialog.showCancelButton(true)
                        pDialog.setCancelClickListener(object : SweetAlertDialog.OnSweetClickListener{
                            override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                                pDialog.dismissWithAnimation()
                            }
                        })
                        pDialog.setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener{
                            override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                                prefIsLogin = false
                                prefIsDashboardTutorial = false
                                prefIsTaskTutorial = false
                                prefUserID = ""
                                prefUserDisplayName = ""
                                prefUserProfilePic = ""
                                prefUserEmail = ""

                                val realm: Realm = Realm.getDefaultInstance()
                                realm.beginTransaction()
                                realm.deleteAll()

                                realm.commitTransaction()
                                startActivity(Intent(this@DashboardActivity, AuthActivity::class.java))
                                finish()
                                ActivityCompat.finishAffinity(this@DashboardActivity)

                                Toaster.showToast(this@DashboardActivity, "Logout successfully", Toaster.State.SUCCESS,Toast.LENGTH_SHORT)
                            }
                        })
                        pDialog.show()



                    }
                }
            }

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

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        } else {

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    doubleBackToExitPressedOnce = false
                }
            }, 2000)

        }
    }

}