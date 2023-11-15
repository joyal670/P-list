package com.iroid.jeetmeet.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.iroid.jeetmeet.R


class TutorialDialogFragment : DialogFragment()
{

    val TAG = "example_dialog"
    var click = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.something_went_wrong, container, false)

        val layout1 = view.findViewById<LinearLayout>(R.id.layout1)
        val layout2 = view.findViewById<LinearLayout>(R.id.layout2)

        view.setOnClickListener {
            if(click == 0)
            {
                layout1.visibility = View.GONE
                layout2.visibility = View.VISIBLE
                click = 1
            }
            else if(click == 1)
            {
                dismiss()
            }
            else
            {
                dismiss()
            }


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