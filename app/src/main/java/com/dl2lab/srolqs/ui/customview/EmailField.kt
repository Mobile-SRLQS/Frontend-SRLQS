package com.dl2lab.srolqs.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class EmailField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                error = if (!isValidEmail(s)) {
                    "Format email tidak valid"
                } else {
                    null
                }
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun isValidEmail(email: CharSequence?): Boolean {
        return email?.let {
            !TextUtils.isEmpty(it) && Patterns.EMAIL_ADDRESS.matcher(it).matches()
        } ?: false
    }
}