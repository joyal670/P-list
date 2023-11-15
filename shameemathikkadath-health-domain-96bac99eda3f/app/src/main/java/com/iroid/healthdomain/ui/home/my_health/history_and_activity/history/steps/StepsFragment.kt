package com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.steps

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.index.Target
import com.iroid.healthdomain.data.model_class.index.Values
import com.iroid.healthdomain.databinding.StepsFragmentBinding
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.hds.HdsFragment
import kotlin.math.roundToInt

class StepsFragment(private val filter: Int) : Fragment() {

    companion object {

        fun newInstance(filter: Int) = StepsFragment(filter)
        var list: List<Target> = arrayListOf()
    }

    private lateinit var viewModel: StepsViewModel
    private lateinit var binding: StepsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(layoutInflater,R.layout.steps_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StepsViewModel::class.java)

        if (HdsFragment.list != null) {
            if (list[1].name.equals("STEPS")) {
                initChart(list[1].values, filter)
            }
        }else println("null")
        binding.toggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->



            when (checkedId) {

                R.id.button -> {
                    initChart(HdsFragment.list[0].values, 1)

                }
                R.id.button2 -> {

                    initChart(HdsFragment.list[0].values, 2)

                }
                R.id.button3 -> {

                    initChart(HdsFragment.list[0].values, 3)


                }


            }
        }
    }

    private fun initChart(stepsValue: Values, filter: Int) {
        val cartesian: Cartesian = AnyChart.column()

        val data: MutableList<DataEntry> = ArrayList()

        when (filter) {
            1 -> {
                for (i in stepsValue.WEEK) {
                    data.add(ValueDataEntry(i.name, i.value.toString().toDouble().roundToInt()))
                }
            }
            2 -> {
                for (i in stepsValue.MONTH) {
                    data.add(ValueDataEntry(i.name, i.value.toString().toDouble().roundToInt()))
                }
            }
            3 -> {
                for (i in stepsValue.YEAR) {
                    data.add(ValueDataEntry(i.name, i.value.toString().toDouble().roundToInt()))
                }
            }
        }


        val column: Column = cartesian.column(data)
        cartesian.animation(true)
        cartesian.labels().enabled(true)
        cartesian.yScale().minimum(0)
        cartesian.yScale().maximum(10000)



        binding.hdsCart.setChart(cartesian)
    }

}
