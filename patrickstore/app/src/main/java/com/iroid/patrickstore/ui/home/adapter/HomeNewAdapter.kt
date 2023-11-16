package com.iroid.patrickstore.ui.home.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.*
import com.iroid.patrickstore.model.home.*
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.home.fragment.FoodFragment
import com.iroid.patrickstore.ui.home.fragment.ShopFragment
import com.iroid.patrickstore.ui.viewall.ViewAllActivity
import com.iroid.patrickstore.utils.Constants.VIEW_ADDS
import com.iroid.patrickstore.utils.Constants.VIEW_BANNER
import com.iroid.patrickstore.utils.Constants.VIEW_PRODUCT_CATEGORY
import com.iroid.patrickstore.utils.Constants.VIEW_SHOP
import com.iroid.patrickstore.utils.Constants.VIEW_SLIDER
import com.iroid.patrickstore.utils.MarginItemDecoration
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


class HomeNewAdapter(
    private val context: Context,
    private val homeList: List<Home>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_BANNER = 1
        const val VIEW_TYPE_SLIDER = 2
        const val VIEW_TYPE_SHOP = 3
        const val VIEW_TYPE_PRODUCT = 4
        const val VIEW_TYPE_EMPTY = 5
        const val VIEW_TYPE_ADDS = 6
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_BANNER -> {
                return BannerViewHolder(
                    ItemViewBannerBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_SLIDER -> {
                return SliderViewHolder(
                    ItemViewSliderBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_ADDS -> {
                return AddViewHolder(
                    ItemViewSliderBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_SHOP -> {
                return ShopViewHolder(
                    ItemViewShopBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_PRODUCT -> {
                return ProductViewHolder(
                    ItemViewProductBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )

            }
            VIEW_TYPE_EMPTY -> {
                return EmptyViewHolder(
                    ItemEmptyBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )

            }
            else -> {
                return BannerViewHolder(
                    ItemViewBannerBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (homeList[position].item) {
            VIEW_BANNER -> {
                if(homeList[position].banner.isNotEmpty()){
                    (holder as BannerViewHolder).bind(homeList[position].banner)
                }

            }
            VIEW_SLIDER -> {
                if(homeList[position].slider.isNotEmpty()){
                    holder.setIsRecyclable(false)
                    (holder as SliderViewHolder).bind(
                        homeList[position].slider,
                        homeList[position].title
                    )
                }

            }
            VIEW_SHOP -> {
                if(homeList[position].shop.isNotEmpty()){
                    (holder as ShopViewHolder).bind(homeList[position].shop)
                }

            }
            VIEW_PRODUCT_CATEGORY -> {
                if(homeList[position].productCategory.isNotEmpty()){
                    (holder as ProductViewHolder).bind(homeList[position].productCategory)
                }

            }
            VIEW_ADDS -> {
                if(homeList[position].adds.isNotEmpty()){
                    (holder as AddViewHolder).bind(homeList[position].adds,"")
                }

            }
            else -> {
                if(homeList[position].banner.isNotEmpty()){
                    (holder as BannerViewHolder).bind(homeList[position].banner)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return homeList.size

    }

    override fun getItemViewType(position: Int): Int {
        when (homeList[position].item) {
            VIEW_BANNER -> {
                return if(homeList[position].banner.isNotEmpty()){
                    VIEW_TYPE_BANNER
                }else{
                    VIEW_TYPE_EMPTY
                }

            }
            VIEW_SLIDER -> {
                return if(homeList[position].slider.isNotEmpty()){
                    VIEW_TYPE_SLIDER
                }else{
                    VIEW_TYPE_EMPTY
                }

            }
            VIEW_ADDS-> {
                return if(homeList[position].adds.isNotEmpty()){
                    VIEW_TYPE_ADDS
                }else{
                    VIEW_TYPE_EMPTY
                }

            }
            VIEW_SHOP -> {
                return if(homeList[position].shop.isNotEmpty()){
                    VIEW_TYPE_SHOP
                }else{
                    VIEW_TYPE_EMPTY
                }
            }
            VIEW_PRODUCT_CATEGORY -> {
                return if(homeList[position].productCategory.isNotEmpty()){
                    VIEW_TYPE_PRODUCT
                }else{
                    VIEW_TYPE_EMPTY
                }

            }
            else -> {
                if(homeList[position].banner.isNotEmpty()){
                    return VIEW_TYPE_BANNER
                }else{
                    return VIEW_TYPE_EMPTY
                }
            }
        }

    }

    private inner class BannerViewHolder(var binding: ItemViewBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: List<Banner>) {
            if (banner.isNotEmpty()) {
                binding.cardBanner.visibility = View.VISIBLE
                val adapter = SliderHome(context, banner, {

                })
                binding.sliderViewHome.setSliderAdapter(adapter)
                binding.sliderViewHome.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                binding.sliderViewHome.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
                binding.sliderViewHome.indicatorSelectedColor = Color.WHITE
                binding.sliderViewHome.indicatorUnselectedColor = Color.GRAY
                binding.sliderViewHome.startAutoCycle()

            }
        }
    }
    private inner class EmptyViewHolder(var binding: ItemEmptyBinding):RecyclerView.ViewHolder(binding.root){

    }

    private inner class SliderViewHolder(var binding: ItemViewSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(slider: List<Slider>, title: String) {
            if (title.isNullOrEmpty()) {
                if (slider.isNotEmpty()) {
                    binding.rvOffer.visibility = View.VISIBLE
                    binding.constraintFestival.visibility = View.GONE
                    val itemDecoration: RecyclerView.ItemDecoration = MarginItemDecoration(
                        context.resources.getDimension(R.dimen.margin_small).toInt()
                    )
                    binding.rvOffer.removeItemDecoration(itemDecoration)
                    binding.rvOffer.addItemDecoration(itemDecoration)
                    binding.rvOffer.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    binding.rvOffer.adapter = OfferAdapter(context, slider)
                }

            } else {
                if (slider.isNotEmpty()) {
                    binding.rvOffer.visibility = View.GONE
                    binding.constraintFestival.visibility = View.VISIBLE
                    binding.tvProductName.visibility = View.VISIBLE
                    binding.tvProductName.text = title
                    val itemDecoration: RecyclerView.ItemDecoration = MarginItemDecoration(
                        context.resources.getDimension(R.dimen.margin_small).toInt()
                    )
                    binding.rvFestival.removeItemDecoration(itemDecoration)
                    binding.rvFestival.addItemDecoration(itemDecoration)
                    binding.rvFestival.layoutManager =
                        GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                    binding.rvFestival.adapter = FestivalAdapter(context, slider)
                }

            }

        }
    }
    private inner class AddViewHolder(var binding: ItemViewSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(slider: List<Adds>, title: String) {

            if (title.isNullOrEmpty()) {
                if (slider.isNotEmpty()) {
                    binding.rvOffer.visibility = View.VISIBLE
                    binding.constraintFestival.visibility = View.GONE

                    val itemDecoration: RecyclerView.ItemDecoration = MarginItemDecoration(
                        context.resources.getDimension(R.dimen.margin_small).toInt()
                    )
                    binding.rvOffer.removeItemDecoration(itemDecoration)
                    binding.rvOffer.addItemDecoration(itemDecoration)
                    binding.rvOffer.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    binding.rvOffer.adapter = AddAdapter(context, slider)
                }

            } else {
                if (slider.isNotEmpty()) {
                    binding.rvOffer.visibility = View.GONE
                    binding.constraintFestival.visibility = View.VISIBLE
                    binding.tvProductName.visibility = View.VISIBLE
                    binding.tvProductName.text = title
                    val itemDecoration: RecyclerView.ItemDecoration = MarginItemDecoration(
                        context.resources.getDimension(R.dimen.margin_small).toInt()
                    )
                    binding.rvFestival.removeItemDecoration(itemDecoration)
                    binding.rvFestival.addItemDecoration(itemDecoration)
                    binding.rvFestival.layoutManager =
                        GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                    binding.rvFestival.adapter = AddAdapter(context, slider)
                }

            }

        }
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
    private inner class ProductViewHolder(var binding: ItemViewProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productCategory: List<ProductCategory>) {
            var categoryId="0"
            var categoryName=""
            var fragmentList = arrayListOf<Fragment>()
            fragmentList.clear()
            productCategory.forEach {
                fragmentList.add(FoodFragment.newInstance(it.name ,it.products))
            }
            val pagerShopAdapter = PagerAdapterFood((context as AppCompatActivity).supportFragmentManager,context.lifecycle,
                fragmentList
            )
            binding.viewAllShop.setOnClickListener {
                val intent = Intent(context, ViewAllActivity::class.java)
                intent.putExtra("id",categoryId)
                intent.putExtra("category_name",categoryName)
                intent.putExtra("type",false)
                context.startActivity(intent)
            }
            binding.viewPagerFood.adapter = pagerShopAdapter
            TabLayoutMediator(binding.tabsFood, binding.viewPagerFood) { tab, position ->
                tab.text=productCategory[position].name


            }.attach()
            binding.viewPagerFood.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    categoryId=productCategory[position]._id
                    categoryName=productCategory[position].name
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    if (position > 0 && positionOffset == 0.0f && positionOffsetPixels == 0) {
                        val view=pagerShopAdapter.getViewAtPosition(position)
                        updatePagerHeightForChild(view!!,binding.viewPagerFood)


                    }
                }
            })


        }
    }

    private inner class ShopViewHolder(var binding: ItemViewShopBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shop: List<Shop>) {
             var fragmentList = arrayListOf<Fragment>()
            fragmentList.clear()
            shop.forEach {
                fragmentList.add(ShopFragment .newInstance(it.name,it.shops))
            }

            val pagerShopAdapter = ShopPagerAdapterShop((context as AppCompatActivity).supportFragmentManager,context.lifecycle,
                fragmentList
            )
            binding.viewPagerShops.adapter = pagerShopAdapter
            var storeId=""
            var shopName=""
            TabLayoutMediator(binding.tabsShop, binding.viewPagerShops) { tab, position ->
                tab.text=shop[position].name


            }.attach()

            binding.viewAllShop.setOnClickListener {
                val intent = Intent(context, ViewAllActivity::class.java)
                intent.putExtra("id",storeId)
                intent.putExtra("category_name",shopName)
                intent.putExtra("type",true)
                context.startActivity(intent)
            }
            binding.viewPagerShops.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    storeId=shop[position]._id
                    shopName=shop[position].name
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                    if (position > 0 && positionOffset == 0.0f && positionOffsetPixels == 0) {
                        val view=pagerShopAdapter.getViewAtPosition(position)
                        updatePagerHeightForChild(view!!,binding.viewPagerShops)


                    }
                }
            })
        }
    }

}
