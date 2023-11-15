package com.mytodo.mytodo.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.mytodo.mytodo.R


class Toaster private constructor(){

    companion object{

        fun showToast(
            context: Context,
            message: String,
            state: State,
            length: Int = Toast.LENGTH_SHORT
        ){

            val inflate =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v: View = inflate.inflate(R.layout.toaster, null)
            val acivStatus : AppCompatImageView = v.findViewById(R.id.acivStatus)
            val actvStatus : AppCompatTextView = v.findViewById(R.id.actvStatus)
            val llToastBackground : LinearLayout = v.findViewById(R.id.llToastBackground)

            when(state){
                State.SUCCESS -> {
                    llToastBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.de_york
                        )
                    )
                    acivStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_warning_white_32dp
                        )
                    )
                }
                State.ERROR -> {
                    llToastBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.torch_red
                        )
                    )
                    acivStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_warning_white_32dp
                        )
                    )
                }
                State.WARNING -> {
                    llToastBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.burnt_sienna
                        )
                    )
                    acivStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_warning_white_32dp
                        )
                    )
                }
            }

            actvStatus.text = message

            val toast = Toast(context)

            toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
            toast.duration = length
            toast.view = v
            toast.show()
        }

    }

    enum class State{
        SUCCESS,
        ERROR,
        WARNING
    }

}

