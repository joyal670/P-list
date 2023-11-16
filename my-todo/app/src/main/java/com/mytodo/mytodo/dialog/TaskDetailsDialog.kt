package com.mytodo.mytodo.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.textview.MaterialTextView
import com.mytodo.mytodo.R


class TaskDetailsDialog(
    private var title: String?,
    private var startDate: String?,
    private var endDate: String?,
    private var description: String?
) : DialogFragment()
{

    val TAG = "example_dialog"
    var click = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.task_deails, container, false)

        val tvTaskname = view.findViewById<MaterialTextView>(R.id.tvTaskname)
        val tvTaskStartDate = view.findViewById<MaterialTextView>(R.id.tvTaskStartDate)
        val tvTaskEndDate = view.findViewById<MaterialTextView>(R.id.tvTaskEndDate)
        val tvTaskDesc = view.findViewById<MaterialTextView>(R.id.tvTaskDesc)

        tvTaskname.text = title
        tvTaskStartDate.text = "Start Date : " +startDate
        tvTaskEndDate.text = "End Date : " +endDate
        tvTaskDesc.text = description

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
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
        }
    }


}