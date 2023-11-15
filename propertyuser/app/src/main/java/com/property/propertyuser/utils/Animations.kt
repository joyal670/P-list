package com.property.propertyuser.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils


object Animations {

    fun scaleAnimation(view: View): ObjectAnimator {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        return animator
    }

    fun shakeAnimation(view: View): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 4f)
        animator.duration = 80
        animator.repeatCount = 3
        animator.repeatMode = ObjectAnimator.REVERSE
        return animator
    }

    fun rotateAnimation(view: View) {
        val animator = ObjectAnimator.ofFloat(
            view, View.ROTATION,
            -360f, 0f
        )
        animator.duration = 1000
        animator.start()
    }

    fun translateAnimation(view: View) {
        val animator = ObjectAnimator.ofFloat(
            view, View.TRANSLATION_X,
            1000f
        )
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    fun scaleAndShakeAnimation(view: View) {
        val scale = scaleAnimation(view)
        scale.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                shakeAnimation(view).start()
            }
        })
        scale.start()
    }

    fun fadeAnimation(view: View) {
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f)
        animator.repeatCount = 3
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    fun animate(context: Context?, animRes: Int, duration: Long, startOffSet: Long, view: View) {
        val animation: Animation = AnimationUtils.loadAnimation(context, animRes)
        animation.duration = duration
        animation.startOffset = startOffSet
        view.startAnimation(animation)
    }

}