package com.example.projectcloud

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_view_assignment.*
import java.net.URLEncoder

class viewAssignment : AppCompatActivity() {
    var assignment = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_assignment)


        assignment = intent.getStringExtra("assignment").toString()
        pdfOpen(assignment)
    }

    private fun pdfOpen(fileUrl: String) {

        webView.webViewClient = WebViewClient()
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true


        var url = ""
        val pdf = fileUrl

        try {
            url = URLEncoder.encode(pdf, "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=$url")

    }

    private class Callback : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}

