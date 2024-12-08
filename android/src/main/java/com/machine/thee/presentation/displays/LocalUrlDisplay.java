package com.machine.thee.presentation.displays;

import android.content.Context;
import android.net.Uri;
import android.view.Display;

import com.machine.thee.presentation.MyWebServer;
import com.machine.thee.presentation.PresentationCallbacks;

import java.io.IOException;

public class LocalUrlDisplay extends UrlDisplay {
    private MyWebServer webServer;

    public LocalUrlDisplay(Context outerContext, Display display, PresentationCallbacks callbacks) {
        super(outerContext, display, callbacks);
    }

    @Override
    public void start(String localPath) {
        if (webView == null) {
            return;
        }

        String path = Uri.parse("http://localhost:8080/" + localPath).toString();

        this.startWebView();

        startWebServer(path);

        webView.loadUrl(path);
    }

    public void startWebServer(String path) {
        if (webServer == null) {
            try {
                webServer = new MyWebServer(this.getContext(), 8080, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
