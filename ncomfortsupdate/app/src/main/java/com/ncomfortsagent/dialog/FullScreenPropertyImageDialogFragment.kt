package com.ncomfortsagent.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.ncomfortsagent.R
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsDocument
import com.ncomfortsagent.utils.loadImagesPropertyWithGlideExt
import kotlin.math.max
import kotlin.math.min


class FullScreenPropertyImageDialogFragment(
    private var context: Activity,
    private var propertyImages: ArrayList<AgentPropertyDetailsDocument>,
) : DialogFragment() {

    private var switcherImageLength: Int = propertyImages.size
    private var counter = 0
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private lateinit var img: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.full_screen_image, container, false)

        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

        img = view.findViewById<ImageView>(R.id.ivFullScreenImg)
        val btn = view.findViewById<ImageView>(R.id.ivBack)
        val ivLeft = view.findViewById<ImageView>(R.id.ivLeft)
        val ivRight = view.findViewById<ImageView>(R.id.ivRight)

        img.loadImagesPropertyWithGlideExt(propertyImages[counter].document)

        ivRight.setOnClickListener {
            counter += 1
            if (counter == switcherImageLength) {
                counter = 0
                img.loadImagesPropertyWithGlideExt(propertyImages[counter].document)
            } else {
                img.loadImagesPropertyWithGlideExt(propertyImages[counter].document)
            }
        }

        ivLeft.setOnClickListener {
            counter += 1
            if (counter == switcherImageLength) {
                counter = 0
                img.loadImagesPropertyWithGlideExt(propertyImages[counter].document)
            } else {
                img.loadImagesPropertyWithGlideExt(propertyImages[counter].document)
            }
        }

        btn.setOnClickListener {
            dismiss()
        }

        return view
    }

    fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(motionEvent)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            img.scaleX = scaleFactor
            img.scaleY = scaleFactor
            return true
        }
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