package com.iroid.healthdomain.ui.home.my_health.history_and_activity.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.iroid.healthdomain.R
import com.iroid.healthdomain.databinding.HistoryFragmentBinding
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.calories.CaloriesFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.hds.HdsFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.steps.StepsFragment
import com.iroid.healthdomain.ui.viewpager_adaptor.ViewPagerAdaptorTwo

class HistoryFragment : Fragment() {


    companion object {
        fun newInstance() = HistoryFragment()

    }

    private lateinit var viewModel: HistoryViewModel
    private lateinit var binding: HistoryFragmentBinding
    private lateinit var adapter: ViewPagerAdaptorTwo
    private lateinit var fragmentList: ArrayList<Fragment>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.history_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)


        fragmentList = arrayListOf<Fragment>(
                HdsFragment.newInstance(1),
                StepsFragment.newInstance(1))


        generateAdaptor(fragmentList)

        TabLayoutMediator(binding.historyTabLayout, binding.historyViewPager) { tab, position ->

            when (position) {
                0 -> tab.text = "HDS"
                1 -> tab.text = "Steps"
//                2 -> tab.text = "Calories"
            }

        }.attach()
        binding.historyViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                val view=adapter.getViewAtPosition(position)
                updatePagerHeightForChild(view!!,binding.historyViewPager)

            }
        })


        binding.toggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->

            var itemPosition: Int = binding.historyViewPager.currentItem

            when (checkedId) {

                R.id.button -> {

                    fragmentList.clear()
                    fragmentList.add(HdsFragment.newInstance(1))
                    fragmentList.add(StepsFragment.newInstance(1))
//                    fragmentList.add(CaloriesFragment.newInstance(1))

                    generateAdaptor(fragmentList)

                    binding.historyViewPager.currentItem = itemPosition

                }
                R.id.button2 -> {

                    fragmentList.clear()
                    fragmentList.add(HdsFragment.newInstance(2))
                    fragmentList.add(StepsFragment.newInstance(2))
//                    fragmentList.add(CaloriesFragment.newInstance(2))

                    generateAdaptor(fragmentList)

                    binding.historyViewPager.currentItem = itemPosition

                }
                R.id.button3 -> {

                    fragmentList.clear()
                    fragmentList.add(HdsFragment.newInstance(3))
                    fragmentList.add(StepsFragment.newInstance(3))
//                    fragmentList.add(CaloriesFragment.newInstance(3))

                    generateAdaptor(fragmentList)

                    binding.historyViewPager.currentItem = itemPosition


                }
            }
        }

    }

    interface OnClick {
        fun onWeekClick()
    }

    fun generateAdaptor(fragmentList: ArrayList<Fragment>) {

        adapter = ViewPagerAdaptorTwo(
                requireActivity().supportFragmentManager,
                lifecycle, fragmentList
        )
        binding.historyViewPager.adapter = adapter

        adapter.notifyDataSetChanged()

    }
    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams)
                    .also { lp ->
                        lp.height = view.measuredHeight
                    }
            }
        }
    }

}
