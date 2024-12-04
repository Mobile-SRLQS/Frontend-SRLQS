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
        // Get the fragment's view as the container
        val fragmentView = fragment.view
        if (fragmentView is ViewGroup) {
            currentContainer = fragmentView

            // Store the content view (the actual fragment content)
            contentView = fragmentView.getChildAt(0)

            // Remove existing loading view if any
            loadingView?.let { existingView ->
                (existingView.parent as? ViewGroup)?.removeView(existingView)
            }

            // Inflate new loading view
            loadingView = LayoutInflater.from(fragment.context).inflate(R.layout.loading_bar, null)

            // Add loading view to fragment's view hierarchy
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            currentContainer?.addView(loadingView, params)

            // Initially hide the loading view
            hide()
        }
    }

    fun init(activity: AppCompatActivity) {
        // Store the content view (the actual activity content)
        contentView = activity.findViewById(android.R.id.content)

        // Remove existing loading view if any
        loadingView?.let { existingView ->
            (existingView.parent as? ViewGroup)?.removeView(existingView)
        }

        // Inflate loading bar layout
        loadingView = activity.layoutInflater.inflate(R.layout.loading_bar, null)

        // Add to the window's decor view
        val window: Window = activity.window
        window.addContentView(loadingView, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ))

        // Initially hide the loading view
        hide()
    }

    fun show() {
        loadingView?.post {
            // Hide content first
            contentView?.visibility = View.INVISIBLE
            // Then show loading view
            loadingView?.visibility = View.VISIBLE
        }
    }

    fun hide() {
        loadingView?.post {
            // Hide loading view first
            loadingView?.visibility = View.GONE
            // Then show content
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
