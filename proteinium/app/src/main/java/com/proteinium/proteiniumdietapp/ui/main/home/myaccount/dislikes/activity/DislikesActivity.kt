package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.dislikes.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.flexbox.*
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.pojo.dislike_tags.Tag
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.myaccount.dislikes.adapter.DislikesAdapter
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Constants.INTENT_TYPE
import com.proteinium.proteiniumdietapp.utils.Constants.TYPE_GUEST
import com.proteinium.proteiniumdietapp.utils.Status
import com.proteinium.proteiniumdietapp.utils.isConnected
import kotlinx.android.synthetic.main.activity_dislikes.*
import kotlinx.android.synthetic.main.no_internet.*
import kotlinx.android.synthetic.main.no_subscrption.*
import kotlinx.android.synthetic.main.toolbar_sub.*
import kotlin.properties.Delegates


class DislikesActivity : BaseActivity() {
    private lateinit var dislikesViewModel: DislikesViewModel
    private var selectedTagIdList: ArrayList<Int> = ArrayList()
    private var plan_active_status by Delegates.notNull<Boolean>()
    private var hrs: Int = 0
    override val layoutId: Int
        get() = R.layout.activity_dislikes
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarSub)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.tvDislike)
        tvNoSubscription.text = getString(R.string.no_dislikes)
        dislikesViewModel.fetchTags(AppPreferences.user_id!!)
        selectedTagIdList.clear()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        dislikesViewModel = ViewModelProviders.of(this).get(DislikesViewModel::class.java)
    }

    override fun setupObserver() {
        dislikesViewModel.getTagsResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    mailLayout.visibility = View.VISIBLE
                    noInternetLayoutDislikes.visibility = View.GONE
                    if (it.data?.status!!) {
                        hrs = it.data?.data!!.dislike_duration
                        plan_active_status = it.data?.data!!.plan_active_status
                        if (it.data.data != null) {
                            recycerViewDislikes.visibility = View.VISIBLE
                            noDislikesLayout.visibility = View.GONE
                            flexboxRecyclerViewSetUpTags(it.data.data.tags)
                        } else {
                            recycerViewDislikes.visibility = View.GONE
                            noDislikesLayout.visibility = View.VISIBLE
                        }
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    if (this.isConnected) {
                        CommonUtils.warningToast(this, getString(R.string.something_wrong))

                    } else {
                        mailLayout.visibility = View.GONE
                        noInternetLayoutDislikes.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
        dislikesViewModel.getDisLikeResponse().observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                dismissLoader()
                if (plan_active_status) {
                    val errorMsf = StringBuilder()
                    errorMsf.append(getString(R.string.dislike_error))
                    errorMsf.append(" ")
                    errorMsf.append(hrs)
                    errorMsf.append(" ")
                    errorMsf.append(getString(R.string.hrs))
                    warningToast(this, errorMsf.toString())

                } else {
                    finish()
                }


            }

        })
    }

    fun warningToast(context: Context, message: String) {
        val dialog = context?.let { it1 -> Dialog(it1) }
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent = dialog?.findViewById(R.id.tvContent) as TextView
        tvContent.text = message

        yesBtn.setOnClickListener {
            dialog.dismiss()
            finish()
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

    private fun flexboxRecyclerViewSetUpTags(tagsList: List<Tag>) {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.CENTER
        recycerViewDislikes.layoutManager = layoutManager
        recycerViewDislikes.adapter = DislikesAdapter(tagsList, {
            selectedTagIdList.clear()
            tagsList.forEach {
                if (it.disliked) {
                    selectedTagIdList.add(it.id)
                }
            }
            selectedTagIdList
        }) {
            CommonUtils.warningToast(this,getString(R.string.dislike_limit))
        }
    }


    override fun onClicks() {

        btnRetry.setOnClickListener {
            dislikesViewModel.fetchTags(AppPreferences.user_id!!)
        }

        btnSave.setOnClickListener {
            if (AppPreferences.isLogin) {
                dislikesViewModel.setTag(AppPreferences.user_id!!, selectedTagIdList)

            } else {
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra(INTENT_TYPE, TYPE_GUEST)
                startActivity(intent)
            }

        }
    }

}