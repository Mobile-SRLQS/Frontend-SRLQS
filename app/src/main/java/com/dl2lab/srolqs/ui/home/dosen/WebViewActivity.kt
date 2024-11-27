package com.dl2lab.srolqs.ui.home.dosen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityWebViewBinding
class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val webView: WebView = findViewById(R.id.webView)
        val url = intent.getStringExtra("url") ?: "https://main.srolqs.me/login"

        val allowedDomain = "srolqs.me"
        if (!url.contains(allowedDomain)) {
            // Handle unauthorized domain
            return
        }

        webView.settings.allowFileAccess = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowUniversalAccessFromFileURLs = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    if (!it.contains(allowedDomain)) {
                        // Block akses ke domain lain
                        return true
                    }
                }
                return false
            }
        }

        webView.settings.javaScriptCanOpenWindowsAutomatically = false

        webView.settings.javaScriptEnabled = true

        webView.clearCache(true)
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE

        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }
        }

        webView.addJavascriptInterface(object : Any() {
            @JavascriptInterface
            fun processData(data: String) {
                val sanitizedData = sanitizeInput(data)
            }
        }, "Android")


        webView.loadUrl(url)
    }

    private fun sanitizeInput(input: String): String {
        return input.replace("<script>", "")
            .replace("</script>", "")
    }
}
