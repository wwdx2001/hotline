package com.sh3h.hotline.ui.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class WebActivity extends ParentActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "WebActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //    @BindView(R.id.loading_process)
    SmoothProgressBar mSmoothProgressBar;

    //    @BindView(R.id.web_view)
    WebView mWebView;

    @Inject
    Bus mEventBus;

    private Unbinder mUnbinder;
    private String url;
    private MenuItem searchItem;
    AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercall_web);
        FrameLayout container = (FrameLayout) findViewById(R.id.fl_container);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mEventBus.register(this);
        initToolBar(R.string.text_guard_operate);
        parseParams(savedInstanceState);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(container, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
//        initWebView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constant.WEB_VIEW_URL, url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
//        if (mWebView != null) {
//            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            mWebView.clearHistory();
//
//            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
//            mWebView.destroy();
//            mWebView = null;
//        }
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
        mUnbinder.unbind();
        mEventBus.unregister(this);
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK /*&& mWebView.canGoBack()*/) {
//            mWebView.goBack();
            if (!mAgentWeb.back()) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void parseParams(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                url = intent.getStringExtra(Constant.WEB_VIEW_URL);
            }
        } else {
            url = savedInstanceState.getString(Constant.WEB_VIEW_URL);
        }
//        url = "https://www.yuque.com/sh3h/dm9r87/vdyuaz";
    }

    private void initWebView() {
        mWebView.loadUrl(url);
        //声明WebSettings子类
        WebSettings webSettings = mWebView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
//
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        // 其他细节操作
        if (NetworkUtil.isNetworkConnected(this)) {
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mSmoothProgressBar != null) {
                    mSmoothProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mSmoothProgressBar != null) {
                    mSmoothProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LogUtil.i(TAG, "onReceivedError");
            }
        });

        //设置WebChromeClient类
        mWebView.setWebChromeClient(new WebChromeClient() {

            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
//                WebActivity.this.initToolBar(title);
            }


            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                    //loading.setText(progress);
                } else if (newProgress == 100) {
                    String progress = newProgress + "%";
                    //loading.setText(progress);
                }
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtil.isNullOrEmpty(query)) {
            return false;
        }
        mAgentWeb.getWebCreator().getWebView().findAllAsync(query);
//        mWebView.findAll(query);
//        try {
//            Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
//            m.invoke(mWebView, true);
//        } catch (Throwable ignored) {
//            LogUtil.i(TAG, "ignored");
//        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
