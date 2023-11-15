package com.iroid.jeetmeet.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import com.iroid.jeetmeet.R



class FullScreenImageDialogFragment(
    private var ImgUrl: String,
    private var Name: String
) : DialogFragment() {

    val TAG = "example_dialog"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.full_screen_image, container, false)

        val img = view.findViewById<ImageView>(R.id.ivFullScreenImg)
        val btn = view.findViewById<ImageView>(R.id.ivBack)
        val name = view.findViewById<MaterialTextView>(R.id.tvName)

        Glide.with(requireActivity()).load(ImgUrl).into(img)
        name.text = Name

        btn.setOnClickListener {
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