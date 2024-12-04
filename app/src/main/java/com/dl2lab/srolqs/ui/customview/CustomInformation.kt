package com.dl2lab.srolqs.ui.customview

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dl2lab.srolqs.R

fun Context.showCustomInformation(
    title: String,
    subtitle: String,
) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_information, null)
    dialogView.background = ContextCompat.getDrawable(this, R.drawable.rounded_dialog_background)

    val dialogTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
    val dialogSubtitle = dialogView.findViewById<TextView>(R.id.dialog_subtitle)
    val btnClose = dialogView.findViewById<ImageButton>(R.id.btn_close)

    val dialogBuilder = AlertDialog.Builder(this).setView(dialogView)
    dialogTitle.text = title
    dialogSubtitle.text = subtitle

    val dialog = dialogBuilder.create()

    btnClose.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}
