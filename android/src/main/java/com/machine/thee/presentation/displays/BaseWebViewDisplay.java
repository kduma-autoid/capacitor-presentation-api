package com.machine.thee.presentation.displays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.machine.thee.presentation.PresentationCallbacks;
import com.machine.thee.presentation.R;

import org.json.JSONException;
import org.json.JSONObject;

abstract public class BaseWebViewDisplay extends BaseDisplay {
    protected final PresentationCallbacks callbacks;
    protected WebView webView;

    public BaseWebViewDisplay(Context outerContext, Display display, PresentationCallbacks callbacks) {
        super(outerContext, display);
        this.callbacks = callbacks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = findViewById(R.id.secondary_webview);
        webView.setVisibility(View.VISIBLE);
    }

    public void sendMessage(JSObject jsonData) {
        if (webView == null) {
            return;
        }

        webView.post(() -> {
            webView.evaluateJavascript("javascript:window.receiveFromPresentationCapacitor(" + jsonData.toString() + ")", null);
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void startWebView() {
        if (webView == null) {
            return;
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(new MessageEvents(), "presentationCapacitor");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String _url) {
                callbacks.onSuccessLoadUrl(webView, _url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    callbacks.onFailLoadUrl(webView, error.getErrorCode());
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
    }

    private class MessageEvents {
        @JavascriptInterface
        public void sendMessage(String jsonData) throws JSONException {
            Log.d("presentationDisplays", "sendMessage(JSONObject)="+ jsonData);
            callbacks.onMessage(new JSObject(jsonData));
        }
    }
}
