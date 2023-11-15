package com.iroid.patrickstore.ui.terms_and_conditions

import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityTermsAndConditionBinding

class TermsAndConditionActivity :
    BaseActivity<TermsAndConditionViewModel, ActivityTermsAndConditionBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_terms_and_condition
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun initViews() {

    }

    override fun getViewBinding(): ActivityTermsAndConditionBinding {
        return ActivityTermsAndConditionBinding.inflate(layoutInflater)
    }

}