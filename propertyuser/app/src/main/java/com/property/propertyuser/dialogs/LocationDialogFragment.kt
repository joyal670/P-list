package com.property.propertyuser.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.property.propertyuser.R


class LocationDialogFragment() : DialogFragment() {

    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle? ,
    ) : View {
        super.onCreateView(inflater , container , savedInstanceState)

        val view : View = inflater.inflate(R.layout.layout_no_loaction_found , container , false)

        dialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog!!.window!!.statusBarColor = Color.parseColor("#2F80ED")
        /*val tryAgain = view.findViewById<MaterialButton>(R.id.tryAgain)
        if(requireContext().isConnected)
        {
            tryAgain.setOnClickListener {
                this.dismiss()
            }
        }*/

        return view
    }


    override fun onStart() {
        super.onStart()
        val dialog : Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width , height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


}