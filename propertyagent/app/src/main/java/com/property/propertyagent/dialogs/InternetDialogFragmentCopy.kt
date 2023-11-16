package com.property.propertyagent.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.property.propertyagent.R
import com.property.propertyagent.utils.AppPreferences.login_type


class InternetDialogFragmentCopy(private var context: Activity) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val view: View = inflater.inflate(R.layout.layout_no_network, container, false)

        val btn = view.findViewById<MaterialButton>(R.id.btnTryAgain)

       if(login_type == "1")
       {
           btn.setBackgroundColor(Color.parseColor("#2F80ED"))
       }else{
           btn.setBackgroundColor(Color.parseColor("#22BD64"))
       }

        btn.setOnClickListener {
            this.dismiss()
            val intent = context.intent
            startActivity(intent)
            context.finish()
            context.overridePendingTransition(0, 0)
        }

        dialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog!!.window!!.statusBarColor = Color.parseColor("#2F80ED")

        return view
    }


    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


}