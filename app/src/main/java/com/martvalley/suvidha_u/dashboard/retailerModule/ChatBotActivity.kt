package com.martvalley.suvidha_u.dashboard.retailerModule

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.martvalley.suvidha_u.R

class ChatBotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        val webView: WebView = findViewById(R.id.webview)

        // Enable JavaScript for WebView
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Load the Tawk.to chat URL
        val chatUrl = "https://tawk.to/chat/66e0077050c10f7a00a6c302/1i7dhq457"
        webView.loadUrl(chatUrl)
    }
}