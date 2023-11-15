package com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.hds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.index.Target
import com.iroid.healthdomain.data.model_class.index.Values
import com.iroid.healthdomain.databinding.HdsFragmentBinding
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.steps.StepsFragment
import kotlin.math.roundToInt


class HdsFragment(private val filter: Int) : Fragment() {


    companion object {

        fun newInstance(filter: Int) = HdsFragment(filter)
        var list: List<Target> = arrayListOf()

    }

    private lateinit var viewModel: HdsViewModel
    private lateinit var binding: HdsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.hds_fragment, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HdsViewModel::class.java)


        if (list != null) {
            if (list[0].name.equals("HDS")) {
                initChart(list[0].values, filter)
            }
        }
        binding.toggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->



            when (checkedId) {

                R.id.button -> {
                    initChart(list[0].values, 1)

                }
                R.id.button2 -> {

                    initChart(list[0].values, 2)

                }
                R.id.button3 -> {

                    initChart(list[0].values, 3)


                }
            }
        }


    }

    private fun initChart(hdsValue: Values, filter: Int) {
        val cartesian: Cartesian = AnyChart.column()

        val data: MutableList<DataEntry> = ArrayList()

        when (filter) {
            1 -> {
                for (i in hdsValue.WEEK) {
                    if(i.value==0){
                        data.add(ValueDataEntry(i.name, -1))
                    }else{
                        data.add(ValueDataEntry(i.name, i.value.toString().toDouble().roundToInt()))
                    }

                    val end = DataEntry()
                }
            }
            2 -> {
                for (i in hdsValue.MONTH) {
                    data.add(ValueDataEntry(i.name, i.value.toString().toDouble().roundToInt()))
                }
            }
            3 -> {
                for (i in hdsValue.YEAR) {
                    data.add(ValueDataEntry(i.name, i.value.toString().toDouble().roundToInt()))
                }
            }
        }


        val column: Column = cartesian.column(data)
        cartesian.animation(true)

        cartesian.yScale().minimum(0)
        cartesian.yScale().maximum(100)
        cartesian.labels().enabled(true)
        binding.hdsCart.setChart(cartesian)
    }


}
