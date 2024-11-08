package com.dl2lab.srolqs.ui.customview

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dl2lab.srolqs.R

object LoadingManager {
    private var loadingView: View? = null

    fun init(activity: AppCompatActivity) {
        // Inflate loading bar layout
        loadingView = activity.layoutInflater.inflate(R.layout.loading_bar, null)

        // Add to the window's decor view
        val window: Window = activity.window
        window.addContentView(loadingView, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT))
    }

    fun show() {
        loadingView?.visibility = View.VISIBLE
    }

    fun hide() {
        loadingView?.visibility = View.GONE
    }

    fun showInFragment(fragment: Fragment) {
        loadingView?.visibility = View.VISIBLE
    }

    fun hideInFragment(fragment: Fragment) {
        loadingView?.visibility = View.GONE
    }
}