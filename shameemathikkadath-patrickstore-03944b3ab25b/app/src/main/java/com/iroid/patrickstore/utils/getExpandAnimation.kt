package com.iroid.patrickstore.utils

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

fun View.getExpandAnimation(): Animation {
        measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST)
        )
        val targetHeight = measuredHeight

        layoutParams.height = 1
        visibility = View.VISIBLE
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                transformation: Transformation?
            ) {
                layoutParams.height =
                    if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }

            override fun cancel() {
                super.cancel()
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }

        animation.duration = 200
        return animation
    }