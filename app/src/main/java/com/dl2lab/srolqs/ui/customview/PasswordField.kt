package com.dl2lab.srolqs.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern

class PasswordField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    // Regular expressions for checking the presence of a number and an uppercase letter
    private val numberPattern = Pattern.compile("[0-9]")
    private val upperCasePattern = Pattern.compile("[A-Z]")

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password = s.toString()

                // Check for password length
                if (password.length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                }
                // Check for at least one number
                else if (!numberPattern.matcher(password).find()) {
                    setError("Password harus mengandung setidaknya 1 angka dan 1 huruf kapital", null)
                }
                // Check for at least one uppercase letter
                else if (!upperCasePattern.matcher(password).find()) {
                    setError("Password harus mengandung setidaknya 1 angka dan 1 huruf kapital", null)
                }
                // Clear error if all conditions are satisfied
                else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}
