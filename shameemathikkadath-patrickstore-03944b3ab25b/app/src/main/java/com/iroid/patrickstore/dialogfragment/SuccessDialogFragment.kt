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
import com.iroid.patrickstore.ui.my_account.my_orders.order_details.OrderDetailActivity
import com.iroid.patrickstore.ui.my_account.my_orders.order_details.OrderDetailsFragment
import com.iroid.patrickstore.utils.Constants


class SuccessDialogFragment(private val function: () -> Unit) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.dialog_order_placed, container, false)
        Handler().postDelayed({
                              function.invoke()
        }, Constants.ORDER_DELEY.toLong())
        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        return dialog
    }

}
