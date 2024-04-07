package com.electro.intranet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var urlEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.wb_webView)
        urlEditText = findViewById(R.id.urlEditText)

        webView.settings.javaScriptEnabled = true
        webView.settings.safeBrowsingEnabled = false
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                urlEditText.setText(url)
            }
        }

        // Check if the activity is started with a URL
        val intent = intent
        val action = intent.action
        val data: Uri? = intent.data
        if (Intent.ACTION_VIEW == action && data != null) {
            val url = data.toString()
            webView.loadUrl(url)
        } else {
            // Load a default URL
            val defaultUrl = "https://www.google.com"
            webView.loadUrl(defaultUrl)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_back -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                }
                return true
            }
            R.id.action_refresh -> {
                webView.reload()
                return true
            }
            R.id.action_forward -> {
                if (webView.canGoForward()) {
                    webView.goForward()
                } else {
                    val query = urlEditText.text.toString()
                    if (isValidUrl(query)) {
                        webView.loadUrl(query)
                    } else {
                        val searchUrl = "https://www.google.com/search?q=$query"
                        webView.loadUrl(searchUrl)
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www")
    }
}
