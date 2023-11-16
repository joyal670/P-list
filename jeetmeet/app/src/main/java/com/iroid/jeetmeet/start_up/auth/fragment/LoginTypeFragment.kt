package com.iroid.jeetmeet.start_up.auth.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentLoginTypeBinding
import com.iroid.jeetmeet.utils.replaceFragment

class LoginTypeFragment : BaseFragment()
{
    private lateinit var binding: FragmentLoginTypeBinding

    override fun setView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      binding = FragmentLoginTypeBinding.inflate(inflater!!,container,false)
        return binding.root
    }

    override fun initData() {

    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {

        /* changing colors on student button click */
        binding.studentBtn.setOnClickListener {
            binding.studentBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.pomegranate)
            binding.studentBtn.setTextColor(Color.parseColor("#FFFFFF"))
            binding.studentBtn.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.pomegranate)

            binding.parentBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            binding.parentBtn.setTextColor(Color.parseColor("#000000"))
            binding.parentBtn.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.dorado)


            appCompatActivity?.replaceFragment(fragment = LoginFragment.newInstance("Student"), addToBackStack = true)
        }

        /* changing colors on parent button click */
        binding.parentBtn.setOnClickListener {
            binding.parentBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.pomegranate)
            binding.parentBtn.setTextColor(Color.parseColor("#FFFFFF"))
            binding.parentBtn.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.pomegranate)

            binding.studentBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            binding.studentBtn.setTextColor(Color.parseColor("#000000"))
            binding.studentBtn.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.dorado)
            binding.studentBtn.strokeWidth = 2

            appCompatActivity?.replaceFragment(fragment = LoginFragment.newInstance("Parent"), addToBackStack = true)
        }

    }
}