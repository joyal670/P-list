package com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.proteinium.proteiniumdietapp.pojo.menu_day.Extra
import com.proteinium.proteiniumdietapp.pojo.menu_day.MealLimit
import com.proteinium.proteiniumdietapp.pojo.menu_day.MealPlanLimit
import com.proteinium.proteiniumdietapp.pojo.menu_day.MealType
import com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.fragments.MealsFragment

class ViewPagerAdapter(
    supportFragmentManager: FragmentManager,
    private val mealTypes: List<MealType>,
    private val function: (Int, Int, Int) -> Unit,
    private val mealIdTypes: List<String>,
    private val mealLimit: List<MealPlanLimit>,
    private val selectionInvokeInstance: (Int, Int) -> Unit,
    private val dislikedTags: ArrayList<Int>,
    private val combinationList: ArrayList<MealLimit>,
    private val extras: List<Extra>
) : FragmentStatePagerAdapter(supportFragmentManager) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return MealsFragment.newInstance(mealTypes[position].id,mealTypes[position].foods, { food: Int, type: Int,pos:Int ->
            function.invoke(food,type,pos)
        }, mealIdTypes as ArrayList<String>,mealLimit,{ sQty: Int, tQty: Int ->
            selectionInvokeInstance.invoke(sQty,tQty)

        },dislikedTags,combinationList,mealTypes,extras)
    }

    override fun getCount(): Int {
        return mealTypes.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mealTypes[position].name
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}