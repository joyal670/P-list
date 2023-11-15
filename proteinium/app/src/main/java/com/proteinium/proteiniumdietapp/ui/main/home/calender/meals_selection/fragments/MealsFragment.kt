package com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseFragment
import com.proteinium.proteiniumdietapp.pojo.menu_day.*
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.adapter.MealsAdapter
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Constants.ARG_MEAL_ID
import com.proteinium.proteiniumdietapp.utils.Constants.ARG_TYPE
import kotlinx.android.synthetic.main.fragment_meals.*


class MealsFragment : BaseFragment()
{
    private var isActive = false
    private var isCreated = false
    private var isCombination=false
    private var mealCombinationList= ArrayList<MealLimit>()
    private var mealTypes=ArrayList<MealType>()
    private var isLoaded=false

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstancate: Bundle?


    ): View? {
        return inflater?.inflate(R.layout.fragment_meals, container, false)
    }

        companion object {

            private var postion: Int? = null
            private lateinit var  functionFood: (Int, Int, Int) -> Unit
            private lateinit var selectionInvoke:(Int,Int)->Unit
            private var foodIdList: ArrayList<Int> = ArrayList<Int>()
            private lateinit var mealAdapter:MealsAdapter

            fun newInstance(
                position: Int,
                foods: List<Food>,
                function: (Int, Int, Int) -> Unit,
                mealIdTypes: ArrayList<String>,
                mealPlanLimit: List<MealPlanLimit>,
                selectionInvokeInstance: (Int, Int) -> Unit,
                dislikedTags: ArrayList<Int>,
                mealsCombination: ArrayList<MealLimit>,
                mealTypes: List<MealType>,
                extras: List<Extra>

            ) =MealsFragment().apply {
                arguments = Bundle().apply {
                    functionFood=function
                    selectionInvoke=selectionInvokeInstance
                    putInt(ARG_MEAL_ID,position)
                    putStringArrayList(ARG_TYPE, mealIdTypes)
                    putParcelableArrayList(Constants.ARG_FOOD, foods as ArrayList<Food>)
                    putIntegerArrayList(Constants.ARG_TAG_ID,dislikedTags)
                    putParcelableArrayList(
                        Constants.ARG_LIMIT,
                        mealPlanLimit as ArrayList<MealPlanLimit>
                    )
                    putParcelableArrayList(Constants.ARG_COMBINATION,mealsCombination)
                    putParcelableArrayList(Constants.ARG_MEAL_TYPE,mealTypes as ArrayList<MealType>)
                    putParcelableArrayList(Constants.ARG_EXTRAS,extras as ArrayList<Extra>)


                }
            }



    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isActive = isVisibleToUser
        loadData()
    }

    private fun loadData() {
            if(isActive&&isCreated){
                val mealList= requireArguments().get(Constants.ARG_FOOD) as ArrayList<Food>
                val mealType= requireArguments().get(Constants.ARG_TYPE) as ArrayList<String>
                val mealPlanLimit=requireArguments().get(Constants.ARG_LIMIT) as ArrayList<MealPlanLimit>
                val dislikedTag=requireArguments().get(Constants.ARG_TAG_ID) as ArrayList<Int>
                val mealTypeId=requireArguments().get(Constants.ARG_MEAL_ID)
                val extrasList=requireArguments().get(Constants.ARG_EXTRAS) as ArrayList<Extra>
                mealCombinationList=requireArguments().get(Constants.ARG_COMBINATION) as ArrayList<MealLimit>
                mealTypes=requireArguments().get(Constants.ARG_MEAL_TYPE) as ArrayList<MealType>
                if(mealList.isNotEmpty()){
                    mailLayout.visibility=View.VISIBLE
                    noMeals.visibility=View.GONE
                    foodIdList.clear()
                    var qty=0
                    mealPlanLimit.forEach {
                        if(it.meal_type_id==mealTypeId.toString()){
                            qty=it.quantity.toInt()
                        }
                    }
                    mealCombinationList.forEach {
                        if(it.combination1==mealTypeId){
                            isCombination=true

                            qty=0
                            mealPlanLimit.forEach { limit->
                                if(limit.meal_type_id==it.combination1.toString()){
                                    qty += limit.quantity.toInt()
                                    getFoodCount(it.combination1)
                                }
                                if(limit.meal_type_id==it.combination2.toString()){
                                    qty += limit.quantity.toInt()
                                    getFoodCount(it.combination2)
                                }
                            }
                            extrasList.forEach {extras->
                                if(extras.meal_type_id==it.combination2){
                                    qty+=extras.quantity.toInt()
                                }
                            }
                        }
                        if(it.combination2==mealTypeId){
                            isCombination=true
                            qty=0
                            mealPlanLimit.forEach { limit->
                                if(limit.meal_type_id==it.combination1.toString()){
                                    qty += limit.quantity.toInt()
                                    getFoodCount(it.combination1)
                                }
                                if(limit.meal_type_id==it.combination2.toString()){
                                    qty += limit.quantity.toInt()
                                    getFoodCount(it.combination2)
                                }
                            }
                            extrasList.forEach {extras->
                                if(extras.meal_type_id==it.combination2){
                                    qty+=extras.quantity.toInt()
                                }
                            }
                        }

                    }
                    if(!isCombination){
                        extrasList.forEach {
                            if(it.meal_type_id==mealTypeId){
                                qty+=it.quantity.toInt()
                            }
                        }
                        mealList.forEach {
                            if(it.ordered_status){
                                foodIdList.add(it.id)
                                functionFood.invoke(it.id, 1, 0)
                            }
                        }
                    }

                    selectionInvoke(foodIdList.size,qty)
                    rvMeals.layoutManager = GridLayoutManager(requireActivity(),2,LinearLayoutManager.VERTICAL,false)
                    mealAdapter= MealsAdapter(
                        mealList, requireContext(),{ id: Int, pos: Int ->

                            if(foodIdList.contains(id) || mealList[pos].ordered_status){
                                Log.e("123", "loadData: $id" )
                                foodIdList.remove(id)
                                mealList[pos].ordered_status=false
                                functionFood.invoke(id, 0, 0)
                                mealAdapter.notifyDataSetChanged()
                                selectionInvoke(foodIdList.size,qty)
                            }else{
                                if(mealType.contains(mealTypeId.toString())){
                                    val tagDec= StringBuilder()
                                    tagDec.append(getString(R.string.selected_tag))
                                    tagDec.append(" ")
                                    if(isCombination) {
                                        foodIdList.clear()
                                        mealTypes.forEach { type ->
                                            mealCombinationList.forEach { combination ->
                                                if (combination.combination1 == type.id) {
                                                    type.foods.forEach { food ->
                                                        if (food.ordered_status) {
                                                            foodIdList.add(food.id)
                                                        }
                                                    }

                                                }
                                                if (combination.combination2 == type.id) {
                                                    type.foods.forEach { food ->
                                                        if (food.ordered_status) {
                                                            foodIdList.add(food.id)
                                                        }
                                                    }

                                                }

                                            }
                                        }
                                    }

                                    if(foodIdList.size>=qty){
                                        showRemoveItemDialog()
                                    }else{
                                        var containTag=0
                                        var tagSize=0
                                        mealList[pos].tags.forEach {tag->
                                            if(dislikedTag.contains(tag.id)){
                                                tagSize += 1
                                            }
                                        }
                                        var pos1=1
                                        mealList[pos].tags.forEach { tag->

                                            Log.e("123", "loadData: $tagSize" )
                                            if(tagSize==1){
                                                if(dislikedTag.contains(tag.id)){
                                                    tagDec.append(tag.name)
                                                    tagDec.append(".")
                                                    containTag=1
                                                }
                                            }
                                            if(tagSize==2){
                                                if(dislikedTag.contains(tag.id)){
                                                    tagDec.append(tag.name)
                                                    if(pos1==1){
                                                        pos1=1+pos1
                                                        tagDec.append(" ")
                                                        tagDec.append(getString(R.string.and))
                                                        tagDec.append(" ")
                                                    }else{
                                                        tagDec.append(".")
                                                    }
                                                    containTag=1
                                                }

                                            }
                                            if(tagSize.toInt() >=3){
                                                if(dislikedTag.contains(tag.id)){
                                                    tagDec.append(tag.name)
                                                    if(pos1==tagSize){
                                                        pos1=pos1+1
                                                        tagDec.append(".")
                                                    }else if(pos1==tagSize-1){
                                                        pos1 += 1
                                                        tagDec.append(" ")
                                                        tagDec.append(getString(R.string.and))
                                                        tagDec.append(" ")

                                                    }else{
                                                        pos1=pos1+1
                                                        tagDec.append(",")
                                                    }

                                                    containTag=1
                                                }

                                            }
                                        }
                                        tagDec.append(getString(R.string.warning_dislike))
                                        if(containTag==1){
                                            val dialog = context?.let { it1 -> Dialog(it1) }
                                            dialog?.setCancelable(true)
                                            dialog?.setContentView(R.layout.alert_meal_selection)

                                            val content=dialog?.findViewById(R.id.tvContent) as TextView
                                            val yesBtn = dialog?.findViewById(R.id.MealSelectionYesBtn) as MaterialButton
                                            val noBtn = dialog?.findViewById(R.id.MealSelectionNoBtn) as MaterialButton
                                            val closeBtn = dialog?.findViewById(R.id.ivMealSelectionClose) as ImageView

                                            content.text=tagDec.toString()
                                            yesBtn.setOnClickListener {
                                                dialog.dismiss()
                                                foodIdList.add(id)
                                                mealList[pos].ordered_status=true
                                                functionFood.invoke(id, 1, 0)
                                                mealAdapter.notifyDataSetChanged()
                                                selectionInvoke(foodIdList.size,qty)
                                            }
                                            closeBtn.setOnClickListener {
                                                dialog.dismiss()
                                            }
                                            noBtn.setOnClickListener {
                                                dialog.dismiss()
                                            }

                                            dialog?.show()
                                            val layoutParams = WindowManager.LayoutParams()
                                            layoutParams.copyFrom(dialog?.window?.attributes)
                                            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                                            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                                            dialog?.window?.attributes = layoutParams
                                            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                        }else{
                                            foodIdList.add(id)
                                            mealList[pos].ordered_status=true
                                            functionFood.invoke(id, 1, 0)
                                            mealAdapter.notifyDataSetChanged()
                                            selectionInvoke(foodIdList.size,qty)
                                        }

                                    }




                                }else{
                                    warningToastMeal(getString(R.string.meal_not_include_food))
                                }

                                functionFood.invoke(id,mealTypeId.toString().toDouble().toInt(), pos)
                            }

                        },{
                            warningToast(getString(R.string.please_subscribe_plan))
                        }
                    )
                    rvMeals.adapter=mealAdapter
                }else{
                    selectionInvoke(0,0)
                    mailLayout.visibility=View.GONE
                    noMeals.visibility=View.VISIBLE
                }
            }



    }

    private fun getFoodCount(combination1: Int) {
        mealTypes.forEach {
            if(combination1==it.id){
                it.foods.forEach {food->
                    if(food.ordered_status){
                        foodIdList.add(it.id)
                        functionFood.invoke(it.id, 1, 0)
                    }
                }
            }
        }

    }

    fun warningToastMeal( message:String){
        val dialog = context?.let { it1 -> Dialog(it1) }
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent=dialog?.findViewById(R.id.tvContent) as TextView
        tvContent.text=message

        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun warningToast( message:String){
        val dialog = context?.let { it1 -> Dialog(it1) }
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent=dialog?.findViewById(R.id.tvContent) as TextView
        tvContent.text=message

        yesBtn.setOnClickListener {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCreated = true;
        loadData();
    }

    override fun onStop() {
        super.onStop()
        isCreated = false

    }
    override fun initData()
    {


    }
    private fun showRemoveItemDialog()
    {
        val dialog = context?.let { it1 -> Dialog(it1) }
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView

        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }



    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }
}
