package com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.calories

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
import com.iroid.healthdomain.databinding.CaloriesFragmentBinding
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.hds.HdsFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.steps.StepsFragment
import kotlin.math.roundToInt

class CaloriesFragment(private val filter: Int) : Fragment() {

    companion object {

        fun newInstance(filter: Int) = CaloriesFragment(filter)

        var list: List<Target> = arrayListOf()
    }

    private lateinit var viewModel: CaloriesViewModel
    private lateinit var binding: CaloriesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.calories_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CaloriesViewModel::class.java)

        if (list != null) {
//            if (list[2].name.equals("CALORIES")) {
//                initChart(StepsFragment.list[2].values, filter)
//            }
        }else println("null")
    }

    private fun initChart(caloriesValue: Values, filter: Int) {
        val cartesian: Cartesian = AnyChart.column()

        val data: MutableList<DataEntry> = ArrayList()



        when (filter) {
            1 -> {
                for (i in caloriesValue.WEEK) {
                    data.add(ValueDataEntry(i.name, i.value.toString().toDouble().roundToInt()))
                }
            }
            2 -> {
                for (i in caloriesValue.MONTH) {
                    data.add(ValueDataEntry(i.name, i.value.toString().toDouble().roundToInt()))
                }
            }
            3 -> {
                for (i in caloriesValue.YEAR) {
                    data.add(ValueDataEntry(i.name, i.value.toString().toDouble().roundToInt()))
                }
            }
        }


        val column: Column = cartesian.column(data)
        cartesian.animation(true)

        cartesian.yScale().minimum(0)
        cartesian.yScale().maximum(100)


        binding.hdsCart.setChart(cartesian)
    }


}