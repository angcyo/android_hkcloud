package com.huika.cloud.util;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WebViewUtil {
  public static void hideBuiltInZoomControls(WebView view) {
    try {
      if (android.os.Build.VERSION.SDK_INT < 11) {
        Field field = WebView.class.getDeclaredField("mZoomButtonsController");
        field.setAccessible(true);
        ZoomButtonsController zoomCtrl = new ZoomButtonsController(view);
        zoomCtrl.getZoomControls().setVisibility(View.GONE);
        field.set(view, zoomCtrl);
      } else {
        WebSettings ws = view.getSettings();
        Method method =
                WebSettings.class.getMethod("setDisplayZoomControls", boolean.class);
        method.invoke(ws, false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
