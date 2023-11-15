package com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.adapter

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.proteinium.proteiniumdietapp.R
import kotlinx.android.synthetic.main.view_poster_overlay.view.*


class PosterOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var onClose: (String) -> Unit = {}

    init {
        View.inflate(context, R.layout.view_poster_overlay, this)
        setBackgroundColor(Color.TRANSPARENT)
    }

    fun update() {
        ivBackBtn.setOnClickListener { onClose("") }
    }

}