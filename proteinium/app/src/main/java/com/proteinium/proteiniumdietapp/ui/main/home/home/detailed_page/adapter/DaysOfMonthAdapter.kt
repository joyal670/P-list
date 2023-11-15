package com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.details_page_date_model.DateModal
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.DateUtils
import kotlinx.android.synthetic.main.calendar_item.view.*
import java.util.*
import kotlin.collections.ArrayList


class DaysOfMonthAdapter(
    private var context: Context,
    private val newDateList: ArrayList<DateModal>,
    private val dateList: MutableList<Date>,
    private val function: (Date, Int) -> Unit,
    private val function1: () -> Unit,
    private val startRange: Int,
    private val currentMonth: Int,
    private val isNonStop: Boolean,
    private val isRenewal: Boolean,
    private val globalSuspension: ArrayList<String>,
    private val functionHoliday: () -> Unit
) : RecyclerView.Adapter<DaysOfMonthAdapter.ViewHold>() {
    inner class ViewHold(itemView: View, private var context: Context) :
        RecyclerView.ViewHolder(itemView) {

        private val weekList= listOf<String>(context.getString(R.string.sun),
            context.getString(R.string.mon),
            context.getString(R.string.tue),
            context.getString(R.string.wed),
            context.getString(R.string.thu),
            context.getString(R.string.fri),
            context.getString(R.string.sat)
        )
        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bindItems(
            data: DateModal,
            date: Date,
            context: Context,
            position: Int,
            startRange: Int,
            currentMonth: Int,
            isNonStop: Boolean,
            isRenewal: Boolean
        ) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, startRange)
            if(!date.before(calendar.time)){
                itemView.visibility=View.GONE
            }
            Log.e("#123","$calendar.")
            itemView.tv_date_calendar_item.text = DateUtils.getDayNumber(date)
            itemView.tv_day_calendar_item.text =CommonUtils.ArabicToEnglish(DateUtils.getDay3LettersName(date),context)

            if(data.isDisabled){
                itemView.alpha=0.5f
                itemView.isEnabled=true
                itemView.visibility=View.GONE
            }else{
                itemView.alpha=1f
                itemView.isEnabled=true
                if (data.isActive) {
                    if(position==0 && isRenewal && (DateUtils.getDay3LettersName(dateList[position]).toString()=="Thu")){
                        if(globalSuspension.contains( DateUtils.getYear(date) + "-" + DateUtils.getMonthNumber(date) + "-" + DateUtils.getDayNumber(
                                date
                            ))){
                            itemView.alpha=0.5f
                            function.invoke(date,position)
                        }else{
                            itemView.cl_calendar_item.background =
                                context.getDrawable(R.drawable.selected_calendar_item_background)
                            itemView.tv_date_calendar_item.setTextColor(context.getColor(R.color.white))
                            itemView.tv_date_calendar_item_dash.setTextColor(context.getColor(R.color.white))
                            itemView.tv_day_calendar_item.setTextColor(context.getColor(R.color.white))
                            function.invoke(date,position)
                        }

                    }else{
                        if(!isNonStop&&(DateUtils.getDay3LettersName(date).toString()=="Fri")){
                            itemView.alpha=0.5f
                        }else{
                            if(globalSuspension.contains( DateUtils.getYear(date) + "-" + DateUtils.getMonthNumber(date) + "-" + DateUtils.getDayNumber(
                                    date
                                ))){
                                itemView.alpha=0.5f
                            }else{

                                itemView.cl_calendar_item.background =
                                    context.getDrawable(R.drawable.selected_calendar_item_background)
                                itemView.tv_date_calendar_item.setTextColor(context.getColor(R.color.white))
                                itemView.tv_date_calendar_item_dash.setTextColor(context.getColor(R.color.white))
                                itemView.tv_day_calendar_item.setTextColor(context.getColor(R.color.white))
                                function.invoke(date,position)
                            }

                        }
                    }



                } else {
                    if(position==0 && isRenewal && (DateUtils.getDay3LettersName(dateList[position]).toString()=="Thu")){
                        if(globalSuspension.contains( DateUtils.getYear(date) + "-" + DateUtils.getMonthNumber(date) + "-" + DateUtils.getDayNumber(
                                date
                            ))){
                            itemView.alpha=0.5f
                        }else{
                            itemView.cl_calendar_item.background =
                                this.context.getDrawable(R.drawable.item_selected_bg)
                            itemView.tv_date_calendar_item.setTextColor(context.getColor(R.color.white))
                            itemView.tv_date_calendar_item_dash.setTextColor(context.getColor(R.color.white))
                            itemView.tv_day_calendar_item.setTextColor(context.getColor(R.color.white))
                        }

                    }else{
                        if(!isNonStop&&(DateUtils.getDay3LettersName(date).toString()=="Fri")){
                            itemView.alpha=0.5f
                        }else{
                            if(globalSuspension.contains( DateUtils.getYear(date) + "-" + DateUtils.getMonthNumber(date) + "-" + DateUtils.getDayNumber(
                                    date
                                ))){
                                itemView.alpha=0.5f
                            }else{
                                itemView.cl_calendar_item.background =
                                    this.context.getDrawable(R.drawable.item_selected_bg)
                                itemView.tv_date_calendar_item.setTextColor(context.getColor(R.color.white))
                                itemView.tv_date_calendar_item_dash.setTextColor(context.getColor(R.color.white))
                                itemView.tv_day_calendar_item.setTextColor(context.getColor(R.color.white))
                            }

                        }
                    }



                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)
        return ViewHold(
            view, context
        )
    }

    override fun getItemCount(): Int {
        return newDateList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.setIsRecyclable(false)
        try{
            if(newDateList[position].isDisabled){
                holder.itemView.alpha=0.5f
                holder.itemView.isEnabled=false
                holder.itemView.visibility=View.GONE

            }
            holder.bindItems(newDateList[position], dateList[position],context,position,startRange,currentMonth,isNonStop,isRenewal)
            holder.itemView.cl_calendar_item.setOnClickListener {
                newDateList.forEach {
                    if (it.date == newDateList[position].date) {
                        if(globalSuspension.contains( DateUtils.getYear(dateList[position]) + "-" + DateUtils.getMonthNumber(dateList[position]) + "-" + DateUtils.getDayNumber(
                                dateList[position]
                            ))){

                            it.isActive = false
                            functionHoliday.invoke()
                        }else{
                            if((DateUtils.getDay3LettersName(dateList[position]).toString()=="Fri")){
                                if(!isNonStop){
                                    it.isActive = false
                                    function1()
                                }else{
                                    it.isActive = !it.isActive
                                }

                            }else{
                                it.isActive = !it.isActive
                            }
                        }



                    } else {
                        it.isActive = false

                    }
                    notifyDataSetChanged()
                }
            }
        }catch (ex:Exception){
            Log.e("exception",ex.toString())
        }



    }
}
