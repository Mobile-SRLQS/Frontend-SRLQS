package com.dl2lab.srolqs.ui.customview

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.dl2lab.srolqs.R

fun Context.showCustomAlertDialog(
    title: String="",
    subtitle: String ,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit
) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
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
