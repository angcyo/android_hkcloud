package com.huika.cloud.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ZoomButtonsController;

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
            WebSettings.class.getMethod("setDisplayZoomControls", new Class[] { boolean.class });
        method.invoke(ws, false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
