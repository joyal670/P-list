package com.property.propertyagent.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.property.propertyagent.R
import com.property.propertyagent.modal.owner.owner_building_details.OwnerBuildingDetailsListDocument
import com.property.propertyagent.utils.loadImagesWithGlideExt


class FullScreenImageDialogFragmentNew(
    private var context: Activity,
    private var propertyImages: ArrayList<OwnerBuildingDetailsListDocument>,
) : DialogFragment() {

    var switcherImageLength: Int = propertyImages.size
    private var counter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.full_screen_image, container, false)

        val img = view.findViewById<ImageView>(R.id.ivFullScreenImg)
        val btn = view.findViewById<ImageView>(R.id.ivBack)
        val ivLeft = view.findViewById<ImageView>(R.id.ivLeft)
        val ivRight = view.findViewById<ImageView>(R.id.ivRight)

        img.loadImagesWithGlideExt(propertyImages[counter].document)

        ivRight.setOnClickListener {
            counter += 1
            if (counter == switcherImageLength) {
                counter = 0
                img.loadImagesWithGlideExt(propertyImages[counter].document)
            } else {
                img.loadImagesWithGlideExt(propertyImages[counter].document)
            }
        }

        ivLeft.setOnClickListener {
            counter += 1
            if (counter == switcherImageLength) {
                counter = 0
                img.loadImagesWithGlideExt(propertyImages[counter].document)
            } else {
                img.loadImagesWithGlideExt(propertyImages[counter].document)
            }
        }

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