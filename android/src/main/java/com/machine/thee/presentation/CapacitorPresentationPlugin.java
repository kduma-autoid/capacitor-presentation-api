package com.machine.thee.presentation;


import android.util.Log;
import android.webkit.WebView;

import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.Locale;



@CapacitorPlugin(name = "CapacitorPresentation")
public class CapacitorPresentationPlugin extends Plugin {
  private final PresentationCallbacks callbacks = new PresentationCallbacks() {
    @Override
    public void onMessage(JSObject jsObject) {
      Log.d("presentationDisplays", "onMessage="+ jsObject);
      notifyListeners("onMessage", jsObject, true);
    }

    @Override
    public void onSuccessLoadUrl(WebView view, String url) {
      Log.d("presentationDisplays", "onSuccessLoadUrl="+url);
      JSObject response = new JSObject();
      response.put("result", url);
      response.put("message", "success");
      notifyListeners("onSuccessLoadUrl", response, true);
    }

    @Override
    public void onFailLoadUrl(WebView view, int errorCode) {
      Log.d("presentationDisplays", "onFailLoadUrl="+errorCode);
      JSObject response = new JSObject();
      response.put("result", errorCode);
      response.put("message", "fail");
      notifyListeners("onFailLoadUrl", response, true);
    }
  };

  private final CapacitorPresentationApi implementation = new CapacitorPresentationApi(this::getContext, this::getActivity, callbacks);

  private OpenType getResultType(String resultType) {
    if (resultType == null) {
      return OpenType.URL;
    }
    try {
      return OpenType.valueOf(resultType.toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException ex) {
      Logger.debug(getLogTag(), "Invalid result type \"" + resultType + "\", defaulting to base64");
      return OpenType.URL;
    }
  }

  @PluginMethod
  public void terminate(PluginCall call) {
    implementation.terminate();
  }

  @PluginMethod
  public void open(PluginCall call) {
    OpenType type = getResultType(call.getString("type"));
    JSObject ret = new JSObject();

    switch (type) {
        case URL:
          implementation.openLink(call.getString("url", null));
            break;
        case VIDEO:
          implementation.openVideo(
                  call.getObject("videoOptions").getString("videoUrl"),
                  Boolean.TRUE.equals(call.getBoolean("showControls"))
          );
            break;
        case HTML:
          implementation.openHtml(call.getString("html", null));
            break;
    }

    call.resolve(ret);
  }

  @PluginMethod
  public void sendMessage(PluginCall call) {
      implementation.sendMessage(call.getObject("data"));
  }

  @PluginMethod
  public void getDisplays(PluginCall call) {
    JSObject response = new JSObject();
    response.put("displays", implementation.getDisplaysCount());
    call.resolve(response);
  }
}
