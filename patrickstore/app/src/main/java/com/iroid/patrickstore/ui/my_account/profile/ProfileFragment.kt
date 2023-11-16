package com.iroid.patrickstore.ui.my_account.profile


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.FragmentProfileBinding
import com.iroid.patrickstore.dialogfragment.RewardDialogFragment
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.preference.AppPreferences.email
import com.iroid.patrickstore.preference.AppPreferences.first_name
import com.iroid.patrickstore.preference.AppPreferences.last_name
import com.iroid.patrickstore.preference.AppPreferences.mobile
import com.iroid.patrickstore.ui.address.AddressActivity
import com.iroid.patrickstore.ui.edit_profile.EditProfileActivity
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.interfaces.FragmentTransInterface
import com.iroid.patrickstore.ui.login.LoginActivity
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.ui.my_account.change_password.ChangePasswordFragment
import com.iroid.patrickstore.ui.my_account.my_orders.MyOrdersFragment
import com.iroid.patrickstore.ui.my_account.my_reviews.MyReviewsActivity
import com.iroid.patrickstore.ui.my_account.reward_points.WalletActivity
import com.iroid.patrickstore.ui.service_order.ServiceOrderActivity
import com.iroid.patrickstore.ui.whisllist.WishListActivity
import com.iroid.patrickstore.utils.Animations
import com.iroid.patrickstore.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentTransInterface = activity as MyAccountActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.my_account))
        binding.constraintMyOrders.setOnClickListener(this)
        binding.constraintMyReviews.setOnClickListener(this)
        binding.constraintMyAddresses.setOnClickListener(this)
        binding.constraintWishlist.setOnClickListener(this)
        binding.constraintRewardPoints.setOnClickListener(this)
        binding.constraintChangePass.setOnClickListener(this)
        binding.constraintLogout.setOnClickListener(this)
        binding.btnGetStart.setOnClickListener(this)
        binding.constraintLayout4.setOnClickListener(this)
        binding.ivEdit.setOnClickListener(this)
        binding.constraintServiceOrder.setOnClickListener(this)

        Animations.scaleAnimation(constraintMyOrders)
        Animations.scaleAnimation(constraintMyReviews)
        Animations.scaleAnimation(constraintMyAddresses)
        Animations.scaleAnimation(constraintWishlist)
        Animations.scaleAnimation(constraintRewardPoints)
        Animations.scaleAnimation(constraintChangePass)
        Animations.scaleAnimation(constraintLogout)
        Animations.scaleAnimation(btnGetStart)
        Animations.scaleAnimation(constraintServiceOrder)
        setUpData()

        setUpAnimation()

    }

    private fun setUpAnimation() {
       /* val renderMyOrders = Render(requireContext())
        renderMyOrders.setAnimation(Attention.Pulse(constraintMyOrders))
        renderMyOrders.setDuration(1000)
        renderMyOrders.start()

        val renderMyReviews = Render(requireContext())
        renderMyReviews.setAnimation(Attention.Pulse(constraintMyReviews))
        renderMyReviews.setDuration(1000)
        renderMyReviews.start()*/

        val animationSlow = AnimationUtils.loadAnimation(context, R.anim.bounce_slow)
        binding.textView38.startAnimation(animationSlow)
        binding.tvSellerText.startAnimation(animationSlow)

        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        animation.duration = 1000
        binding.constraintMyOrders.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                startMyReviewAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private fun startMyReviewAnimation() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        binding.constraintMyReviews.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                startMyAddressesAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private fun startMyAddressesAnimation() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        binding.constraintMyAddresses.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                startWishlistAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private fun startWishlistAnimation() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        binding.constraintWishlist.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                startRewardPointsAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private fun startRewardPointsAnimation() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        binding.constraintRewardPoints.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private fun setUpData() {
        val userName = StringBuilder()
//        userName.append(first_name)
//        userName.append(" ")
//        userName.append(last_name)
//        binding.tvUserName.text = userName.toString()
//        binding.tvMobile.text = mobile
//        binding.tvEmail.text = email

        binding.tvUserName.text = first_name + " " + last_name
        binding.tvMobile.text = mobile
        binding.tvEmail.text = email
        CommonUtils.setImageBase(requireActivity(),AppPreferences.image_url!!,binding.imageView4)

    }

    private fun showRewardDialog() {
        var isLargeLayout = resources.getBoolean(R.bool.large_layout)
        val fragmentManager = requireActivity().supportFragmentManager
        val newFragment = RewardDialogFragment()
        if (isLargeLayout) {
            newFragment.show(fragmentManager, "dialog")
        } else {
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.setImageBase(requireActivity(),AppPreferences.image_url!!,binding.imageView4)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivEdit -> {

                val animation = AnimationUtils.loadAnimation(context, R.anim.bounce_fast)
                binding.ivEdit.startAnimation(animation)
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        startActivity(
                            Intent(
                                activity,
                                EditProfileActivity::class.java
                            )
                        )
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }
                })

            }


            binding.constraintMyOrders -> fragmentTransInterface.replaceFragment(MyOrdersFragment())
            binding.constraintMyReviews ->startActivity(Intent(
                activity,
                MyReviewsActivity::class.java
            ))
            binding.constraintMyAddresses -> startActivity(
                Intent(
                    activity,
                    AddressActivity::class.java
                )
            )
            binding.constraintWishlist -> startActivity(
                Intent(
                    activity,
                    WishListActivity::class.java
                )
            )
            binding.constraintRewardPoints -> startActivity(
                Intent(
                    activity,
                    WalletActivity::class.java
                )
            )
            binding.constraintChangePass -> fragmentTransInterface.replaceFragment(
                ChangePasswordFragment()
            )
            constraintLogout -> logoutDialog()
            btnGetStart -> {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }

            binding.constraintServiceOrder->{
                startActivity(
                    Intent(
                        activity,
                        ServiceOrderActivity::class.java
                    ))
            }

        }
    }

    private fun logoutDialog() {
        val builder = activity?.let { AlertDialog.Builder(it) }
        builder!!.setMessage("Are you sure you want to logout ?")
        builder!!.setPositiveButton("CANCEL") { dialog, which ->
        }
        builder!!.setNegativeButton("LOGOUT") { dialog, which ->
            AppPreferences.cleareSharedPreference()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
