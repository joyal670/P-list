package com.property.propertyuser.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.property.propertyuser.R


class GenericTextWatcher(private val view: View, private val editText: Array<EditText>) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) {
        val text = editable.toString()
        when (view.id) {
            R.id.otp_box1 -> if (text.length == 1) editText[1].requestFocus()
            R.id.otp_box2 -> if (text.length == 1) editText[2]
                .requestFocus() else if (text.isEmpty()) editText[0].requestFocus()
            R.id.otp_box3 -> if (text.length == 1) editText[3]
                .requestFocus() else if (text.isEmpty()) editText[1].requestFocus()
            R.id.otp_box4 -> if (text.isEmpty()) editText[2].requestFocus()
        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) {
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) {
    }

}