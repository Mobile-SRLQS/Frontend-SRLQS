package com.dl2lab.srolqs.ui.customview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dl2lab.srolqs.R


object LoadingManager {
    private var loadingView: View? = null
    private var currentContainer: ViewGroup? = null
    private var contentView: View? = null

    fun init(fragment: Fragment) {
        val fragmentView = fragment.view
        if (fragmentView is ViewGroup) {
            currentContainer = fragmentView

            contentView = fragmentView.getChildAt(0)

            loadingView?.let { existingView ->
                (existingView.parent as? ViewGroup)?.removeView(existingView)
            }

            loadingView = LayoutInflater.from(fragment.context).inflate(R.layout.loading_bar, null)

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            currentContainer?.addView(loadingView, params)

            hide()
        }
    }

    fun init(activity: AppCompatActivity) {
        contentView = activity.findViewById(android.R.id.content)

        loadingView?.let { existingView ->
            (existingView.parent as? ViewGroup)?.removeView(existingView)
        }

        loadingView = activity.layoutInflater.inflate(R.layout.loading_bar, null)

        val window: Window = activity.window
        window.addContentView(loadingView, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ))
        hide()
    }

    fun show() {
        loadingView?.post {
            contentView?.visibility = View.INVISIBLE
            loadingView?.visibility = View.VISIBLE
        }
    }

    fun hide() {
        loadingView?.post {
            loadingView?.visibility = View.GONE
            contentView?.visibility = View.VISIBLE
        }
    }

    fun cleanup() {
        loadingView?.let { existingView ->
            (existingView.parent as? ViewGroup)?.removeView(existingView)
        }
        loadingView = null
        currentContainer = null
        contentView = null
    }
}
