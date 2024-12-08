package com.machine.thee.presentation.displays;

import android.content.Context;
import android.net.Uri;
import android.view.Display;

import com.machine.thee.presentation.PresentationCallbacks;

public class UrlDisplay extends BaseWebViewDisplay {
    public UrlDisplay(Context outerContext, Display display, PresentationCallbacks callbacks) {
        super(outerContext, display, callbacks);
    }

    public void start(String url) {
        if (webView == null) {
            return;
        }

        String path = Uri.parse(url).toString();

        this.startWebView();

        webView.loadUrl(path);
    }
}
