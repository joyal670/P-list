package com.iroid.healthdomain.ui.home.mainActivity.person

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.navigation.NavArgument
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.awesomedialog.*
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.data.model_class.user_challenge.DataX
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.ActivityPersonalBinding
import com.iroid.healthdomain.ui.adaptor.UserPastChallengesAdapter
import com.iroid.healthdomain.ui.base.BaseActivity
import com.iroid.healthdomain.ui.home.mainActivity.HomeFragmentDirections
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import kotlinx.android.synthetic.main.activity_personal.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.Serializable

class PersonActivity :BaseActivity<PersonViewModel, ActivityPersonalBinding, HomeRepository>() {
    var currentFavStatus: Int = 0
    @SuppressLint("RestrictedApi")
    var currentUserId: Int = 0
    var selectedId=0
    private val adapter: UserPastChallengesAdapter by lazy { UserPastChallengesAdapter(currentUserId) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun getFragmentRepository(): HomeRepository {
        GlobalScope.launch {
            currentUserId = runBlocking { userPreferences.userId.first()!! }
        }
        return HomeRepository(remoteDataSource.buildApi(ApiServices::class.java), userPreferences)

    }

    override fun getLayoutRes(): Int = R.layout.activity_personal

    override fun getViewModel(): Class<PersonViewModel> = PersonViewModel::class.java

    @SuppressLint("ResourceType")
    override fun init() {
        val bundle1=intent.extras
        val contactData:ContactData= bundle1!!["Data"] as ContactData



        val navHostFragment = nav_host_fragment_person as NavHostFragment
        val navController = navHostFragment.navController
        val navInflater = navController.navInflater
        val graph: NavGraph = navInflater.inflate(R.navigation.nav_drawer_person)
        val animationOptions = NavOptions.Builder().setEnterAnim(R.anim.nav_default_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim).build()
        graph.addArgument("pass_value", NavArgument.Builder().setDefaultValue(contactData).build()) // This is where you pass the bundle data from Activity to StartDestination
        navHostFragment.navController.graph = graph


    }

}

