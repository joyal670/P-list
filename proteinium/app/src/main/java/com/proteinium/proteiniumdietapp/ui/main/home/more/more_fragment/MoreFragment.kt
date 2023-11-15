package com.proteinium.proteiniumdietapp.ui.main.home.more.more_fragment

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.proteinium.proteiniumdietapp.BuildConfig
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseFragment
import com.proteinium.proteiniumdietapp.listeners.ActivityListener
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.main.home.more.aboutus.AboutUsActivity
import com.proteinium.proteiniumdietapp.ui.main.home.more.contactus.ContactUsActivity
import com.proteinium.proteiniumdietapp.ui.main.home.more.privacypolicy.PrivacyPolicyActivity
import com.proteinium.proteiniumdietapp.ui.main.home.more.termsAndConditions.TermsAndConditionsActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.isConnected
import com.proteinium.proteiniumdietapp.utils.snack
import kotlinx.android.synthetic.main.fragment_more.*
import java.net.URLEncoder


class MoreFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: ActivityListener

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_more, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initData() {
        fragmentTransInterface = activity as HomeActivity
        fragmentTransInterface.setTitle(
            getString(R.string.more_options),
            16f,
            R.font.segoe_ui,
            false
        )
        fragmentTransInterface.setBackButton(false)
        fragmentTransInterface.hideToolbar(false)
        if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
            tvAboutUs.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_account_user),
                null,
                requireActivity().getDrawable(R.drawable.ic_silver_arrow_right),
                null
            )
            tvContactUs.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_contacts_icon),
                null,
                requireActivity().getDrawable(R.drawable.ic_silver_arrow_right),
                null
            )
            tvPrivacyPolicy.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_insurance),
                null,
                requireActivity().getDrawable(R.drawable.ic_silver_arrow_right),
                null
            )
            tvTermsAndConditions.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(
                    R.drawable.ic_terms_and_conditions
                ), null, requireActivity().getDrawable(R.drawable.ic_silver_arrow_right), null
            )
        }
        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
            tvAboutUs.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_baseline_arrow_forward_silver),
                null,
                requireActivity().getDrawable(R.drawable.ic_account_user),
                null
            )
            tvContactUs.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_baseline_arrow_forward_silver),
                null,
                requireActivity().getDrawable(R.drawable.ic_contacts_icon),
                null
            )
            tvPrivacyPolicy.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(R.drawable.ic_baseline_arrow_forward_silver),
                null,
                requireActivity().getDrawable(R.drawable.ic_insurance),
                null
            )
            tvTermsAndConditions.setCompoundDrawablesWithIntrinsicBounds(
                requireActivity().getDrawable(
                    R.drawable.ic_baseline_arrow_forward_silver
                ), null, requireActivity().getDrawable(R.drawable.ic_terms_and_conditions), null
            )
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        tvAboutUs.setOnClickListener {
            startActivity(Intent(requireContext(), AboutUsActivity::class.java))
        }

        tvContactUs.setOnClickListener {
            val packageManager: PackageManager = requireActivity().packageManager
            val i = Intent(Intent.ACTION_VIEW)

            try {
                val url =
                    "https://api.whatsapp.com/send?phone=" +96550903333 + "&text=" + URLEncoder.encode(
                        "Hai",
                        "UTF-8"
                    )
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        tvPrivacyPolicy.setOnClickListener {
            startActivity(Intent(activity, PrivacyPolicyActivity::class.java))
        }

        tvTermsAndConditions.setOnClickListener {
            startActivity(Intent(activity, TermsAndConditionsActivity::class.java))
        }
        tvPoweredBy.setOnClickListener {
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://blackonyxsolutions.com/")
            )
            startActivity(viewIntent)
        }
        facebookBtn.setOnClickListener {
            val packageManager: PackageManager = requireActivity().packageManager
            val i = Intent(Intent.ACTION_VIEW)

            try {
                val url =
                    "https://api.whatsapp.com/send?phone=" +96550903333 + "&text=" + URLEncoder.encode(
                        "Hai",
                        "UTF-8"
                    )
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        twitterBtn.setOnClickListener {
            if (requireContext().isConnected) {
                val twitterIntent = Intent()
                twitterIntent.action = Intent.ACTION_SEND
                twitterIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}


                """.trimIndent()
                twitterIntent.type = "text/plain"
                twitterIntent.setPackage("com.twitter.android")
                twitterIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                try {
                    startActivity(twitterIntent)
                } catch (ex: ActivityNotFoundException) {
                    CommonUtils.warningToast(requireContext(), getString(R.string.error_twitter))
                }
            } else {
                view?.snack(getString(R.string.no_internet))
            }
        }

        instagramBtn.setOnClickListener {
            if (requireContext().isConnected) {

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/proteinium.kw/")))
                } catch (ex: ActivityNotFoundException) {
                    try {


                    } catch (ex: ActivityNotFoundException) {
                        CommonUtils.warningToast(
                            requireContext(),
                            getString(R.string.error_instagram)
                        )
                    }

                }
            } else {
                view?.snack(getString(R.string.no_internet))
            }
        }

        linkiidinBtn.setOnClickListener {
            if (requireContext().isConnected) {
                val twitterIntent = Intent()
                twitterIntent.action = Intent.ACTION_SEND
                twitterIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}


                """.trimIndent()
                twitterIntent.type = "text/plain"
                twitterIntent.setPackage("com.linkedin.android")
                twitterIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                try {
                    startActivity(twitterIntent)
                } catch (ex: ActivityNotFoundException) {
                    try {
                        val url = "https://www.linkedin.com/"
                        val i = Intent("android.intent.action.MAIN")
                        i.component =
                            ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main")
                        i.addCategory("android.intent.category.LAUNCHER")
                        i.data = Uri.parse(url)
                        startActivity(i)
                    } catch (ex: ActivityNotFoundException) {
                        CommonUtils.warningToast(
                            requireContext(),
                            getString(R.string.error_linkedin)
                        )
                    }


                }
            } else {
                view?.snack(getString(R.string.no_internet))
            }
        }
    }
}
