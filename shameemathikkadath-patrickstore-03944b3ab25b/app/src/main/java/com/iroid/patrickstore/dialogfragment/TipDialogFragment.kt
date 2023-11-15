package com.iroid.patrickstore.dialogfragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.iroid.patrickstore.R
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.login.LoginActivity
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.ui.my_account.my_orders.order_details.OrderDetailsFragment
import com.iroid.patrickstore.utils.Constants


class TipDialogFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.dialog_layout_tip, container, false)
        Handler().postDelayed({

            val intent = Intent(activity, MyAccountActivity::class.java)
            intent.putExtra(Constants.INTENT_NAVIGATION,Constants.INTENT_ORDER)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)



        }, Constants.ORDER_DELEY.toLong())
        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        return dialog
    }

}