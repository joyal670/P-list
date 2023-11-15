package com.iroid.emergency.main.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseActivity
import com.iroid.emergency.databinding.ActivitySettingsBinding
import com.iroid.emergency.preference.ConstantPreference


class SettingsActivity : BaseActivity<SettingsViewModal, ActivitySettingsBinding>() {
    private lateinit var bundle: Bundle
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun setOnClick() {

        binding.toolbar.floatingActionButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initViews() {
        bundle= intent.getBundleExtra(ConstantPreference.PASS_BUNDLE)!!
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.settings_navigation)
        binding.toolbar.btnEdit.setOnClickListener {
            binding.toolbar.btnEdit.visibility= View.GONE
            navHostFragment.navController.navigate(R.id.editProfileFragment)
        }
        when(bundle[ConstantPreference.TYPE]){
            ConstantPreference.PROFILE->{
                binding.toolbar.btnEdit.visibility= View.VISIBLE
                graph.setStartDestination(R.id.nav_profile)
            }
            ConstantPreference.SUBMISSION->{
                binding.toolbar.btnEdit.visibility= View.GONE
                graph.setStartDestination(R.id.nav_emergency)
            }
            ConstantPreference.FAQ->{
                binding.toolbar.tvTitle.text=getString(R.string.faq)
                binding.toolbar.btnEdit.visibility= View.GONE
                graph.setStartDestination(R.id.nav_faq)
            }
            ConstantPreference.FEEDBACK->{
                binding.toolbar.tvTitle.text=getString(R.string.feedback)
                binding.toolbar.btnEdit.visibility= View.GONE
                graph.setStartDestination(R.id.feedbackFragment)
            }
            ConstantPreference.ABOUT->{
                binding.toolbar.tvTitle.text=getString(R.string.about)
                binding.toolbar.btnEdit.visibility= View.GONE
                graph.setStartDestination(R.id.aboutFragment)
            }
        }
        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }

    override fun getViewBinding(): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(layoutInflater)
    }
}
