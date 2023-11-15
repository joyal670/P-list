package com.iroid.healthdomain.ui.home.my_health.history_and_activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.iroid.healthdomain.R
import com.iroid.healthdomain.databinding.FragmentHomeViewPagerBinding
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.activity.ActivityFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.HistoryFragment
import com.iroid.healthdomain.ui.viewpager_adaptor.ViewPagerAdaptor

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeViewPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeViewPagerFragment : Fragment()  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    protected lateinit var binding: FragmentHomeViewPagerBinding


    private lateinit var adapterFollower: ViewPagerAdaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            println("Array $it")

           // param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home_view_pager, container, false)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeViewPagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                HomeViewPagerFragment().apply {
                    arguments = Bundle().apply {
                      //  putParcelableArrayList(ARG_PARAM1, targets)
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }

//        var list: List<Target> = arrayListOf()
//        set(value) {
//            field = value
//        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

      //  if (list!= null) HistoryFragment.list = list

//            println("name ${list.get(0).name}")
//
//            if (list.get(0).name.equals("HDS")){
//                val hrsValue = list.get(0).values
//            }



        val fragmentList = arrayListOf<Fragment>(
                HistoryFragment(),
                ActivityFragment()
        )

        adapterFollower = ViewPagerAdaptor(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.homeViewPager.adapter = adapterFollower
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->

            when (position) {
                0 -> {
                    tab.text = "History"

                }
                1 -> {
                    tab.text = "Activities"
                }
            }

        }.attach()

        binding.homeViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                val view=adapterFollower.getViewAtPosition(position)
                updatePagerHeightForChild(view!!,binding.homeViewPager)

            }
        })



//        binding.homeViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                val view = // ... get the view
//                        view.post {
//                            val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
//                            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//                            view.measure(wMeasureSpec, hMeasureSpec)
//
//                            if (binding.homeViewPager.layoutParams.height != view.measuredHeight) {
//                                // ParentViewGroup is, for example, LinearLayout
//                                // ... or whatever the parent of the ViewPager2 is
//                                binding.homeViewPager.layoutParams = (binding.homeViewPager.layoutParams as ViewGroup.LayoutParams)
//                                        .also { lp -> lp.height = view.measuredHeight }
//                            }
//                        }
//            }
//        })


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
