package com.dl2lab.srolqs.ui.customview

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dl2lab.srolqs.R

fun Context.showCustomAlertDialog(
    title: String="",
    subtitle: String ,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    showIcon: Boolean=true,
    error: Boolean=true,
) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
    dialogView.background = ContextCompat.getDrawable(this, R.drawable.rounded_dialog_background)

    val dialogIcon= dialogView.findViewById<ImageView>(R.id.dialog_icon)
    val dialogTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
    val dialogSubtitle = dialogView.findViewById<TextView>(R.id.dialog_subtitle)
    val positiveButton = dialogView.findViewById<Button>(R.id.dialog_positive_button)
    val negativeButton = dialogView.findViewById<Button>(R.id.dialog_negative_button)
    if(negativeButtonText == "" ){
        negativeButton.visibility = View.GONE
    }
    val dialogBuilder = AlertDialog.Builder(this).setView(dialogView)
    if(title == ""){
        dialogTitle.visibility = View.GONE
    } else{
        dialogTitle.text=title
    }

    dialogSubtitle.text = subtitle

    positiveButton.text = positiveButtonText

    if(error){
        dialogIcon.setBackgroundResource(R.drawable.ic_error_icon)
    } else{
        dialogView.setBackgroundResource(R.drawable.ic_success_icon)
    }
    if(showIcon){
        dialogIcon.visibility = View.VISIBLE
    } else{
        dialogIcon.visibility = View.GONE
    }

    val dialog = dialogBuilder.create()

    positiveButton.setOnClickListener {
        onPositiveButtonClick()
        dialog.dismiss()
    }
    if(negativeButtonText == ""){
        negativeButton.visibility = View.GONE
    } else{
        negativeButton.text = negativeButtonText
        negativeButton.setOnClickListener {
            onNegativeButtonClick()
            dialog.dismiss()
        }
    }




    dialog.show()
}
