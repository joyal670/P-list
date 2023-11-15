package com.iroid.patrickstore.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.view.View

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

    fun rotater(view: View) {
        val animator = ObjectAnimator.ofFloat(
            view, View.ROTATION,
            -360f, 0f
        )
        animator.duration = 1000
        animator.start()
    }

    @SuppressLint("WrongConstant")
    fun translater(view: View) {
        val animator = ObjectAnimator.ofFloat(
            view, View.TRANSLATION_Y,
            -1000f
        )
        animator.duration = 1000
        animator.repeatCount = 0
        animator.repeatMode = ObjectAnimator.RESTART
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

    fun fade(view: View) {
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f)
        animator.repeatCount = 3
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }
}