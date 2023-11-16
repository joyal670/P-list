package com.iroid.jeetmeet.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.iroid.jeetmeet.R


class NoDataDialogFragment(
) : DialogFragment() {

    val TAG = "example_dialog"

   /* override fun getTheme(): Int {
        return R.style.DialogTheme
    }
*/


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        //setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
        val view: View = inflater.inflate(R.layout.fragment_nodata, container, false)

        dialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog!!.window!!.statusBarColor = Color.parseColor("#EE2424")

        view.setOnClickListener {
            dismiss()
        }

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
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
        }
    }


}