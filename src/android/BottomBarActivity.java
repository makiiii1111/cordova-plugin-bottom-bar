/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package __PACKAGE_NAME__;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.cordova.*;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;
import org.apache.cordova.CordovaWebView;

import __PACKAGE_NAME__.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.ShySettings;
import com.roughike.bottombar.TabSelectionInterceptor;

import nativewebview.NativeWebView;

public class BottomBarActivity extends CordovaActivity
{
    public static BottomBar bottomBar;
    public bottombar.BottomBar bottomBarPlugin = new bottombar.BottomBar();
    private boolean first_openApp;
    private boolean first_openHome = true;
    private boolean first_openLine = true;
    private boolean first_openMine = true;
    private boolean first_openNews = true;
    private boolean is_mwebview = false;
    private int current_tab = -1;
    private Activity currentActivity = this;
  private static ViewPropertyAnimatorCompat mTranslationAnimator;
  private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
  private static final String APP_CACAHE_DIRNAME = "/webcache";
  private WebView mWebView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }
        setContentView(R.layout.bottom_bar);
        super.init();
        first_openApp = true;
      // Set by <content src="index.html" /> in config.xml
      loadUrl(launchUrl);
      mWebView = findViewById(R.id.webview);
      mWebView.getParent().bringChildToFront(mWebView);
      initWebView();
      bottomBar = (BottomBar) findViewById(R.id.bottomBar);
      bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes int tabId) {
          if(first_openApp==true){
            current_tab = tabId;
            first_openApp = false;
          }else{
            if(tabId==current_tab){
              hideWebView(10);
              return;
            }else{
              switch (tabId){
                case R.id.tab_home:
                  appView.getEngine().loadUrl(launchUrl+"#/root-tabs/home",false);
                    hideWebView(first_openHome?500:150);
                  first_openHome = false;
                  current_tab = R.id.tab_home;
                  break;
                case R.id.tab_line:
                  appView.getEngine().loadUrl(launchUrl+"#/root-tabs/path",false);
                    hideWebView(first_openLine?500:150);
                    first_openLine = false;
                  current_tab = R.id.tab_line;
                  break;
                case R.id.tab_news:
                  if(first_openNews){
                    mWebView.loadUrl(bottomBarPlugin.webViewUrl);
                    first_openNews = false;
                  }
                  showWebView();
                  break;
                case R.id.tab_mine:
                  appView.getEngine().loadUrl(launchUrl+"#/root-tabs/mine",false);
                    hideWebView(first_openMine?500:150);
                    first_openMine = false;
                  current_tab = R.id.tab_mine;
                  break;
              }
            }
          }
        }
      });
    }
    @Override
    protected CordovaWebView makeWebView() {
        SystemWebView webView = (SystemWebView)findViewById(R.id.cordovaWebView);
        return new CordovaWebViewImpl(new SystemWebViewEngine(webView));
    }
    @Override
    protected void createViews() {
        if (preferences.contains("BackgroundColor")) {
            int backgroundColor = preferences.getInteger("BackgroundColor", Color.BLACK);
            // Background of activity:
            appView.getView().setBackgroundColor(backgroundColor);
        }
        appView.getView().requestFocusFromTouch();
    }
    private void showWebView(){
      if(!is_mwebview){
        appView.getView().setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
        is_mwebview = true;
      }
    }
    private void hideWebView(int mill){
      if(is_mwebview){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
            mWebView.setVisibility(View.GONE);
            appView.getView().setVisibility(View.VISIBLE);

            is_mwebview = false;
          }
        }, mill);

      }
    }
  private void initWebView() {
      WebSettings webSettings = mWebView.getSettings();
    mWebView.setWebViewClient(new WebViewClient(){
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        NativeWebView nativeWebView = new NativeWebView(currentActivity,true);
        nativeWebView.showWebPage(url);
        return true;
      }
      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon){
        super.onPageStarted(view, url, favicon);
      }
      @Override
      public void onPageFinished(WebView view, String url){
        super.onPageFinished(view, url);
      }
    });

    webSettings.setJavaScriptEnabled(true);

  }


}
