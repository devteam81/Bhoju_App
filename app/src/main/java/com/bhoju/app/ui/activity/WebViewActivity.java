package com.bhoju.app.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bhoju.app.R;
import com.bhoju.app.network.APIConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends BaseActivity {

    private final String TAG = WebViewActivity.class.getSimpleName();
    public static final String PAGE_TYPE = "pageType";
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.progress)
    ProgressBar progress;
    APIInterface apiInterface;
    @BindView(R.id.staticText)
    TextView staticText;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        img_back.setOnClickListener(v -> onBackPressed());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Intent caller = getIntent();
        int pageType = caller.getIntExtra(PAGE_TYPE, PageTypes.ABOUT);
        String pageUrl= caller.getStringExtra("URL");

        setUpWebViewDefaults();
        setUpRequiredWebPage(pageType,pageUrl);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebViewDefaults() {
        WebSettings webSettings = webView.getSettings();
        WebView.setWebContentsDebuggingEnabled(true);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                txt_title.setText(view.getTitle());
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                switch (newProgress) {
                    case 0:
                        progress.setVisibility(View.VISIBLE);
                        break;
                    case 100:
                        progress.setVisibility(View.GONE);
                        break;
                    default:
                        progress.setProgress(newProgress);
                }
            }
        });
    }

    private void setUpRequiredWebPage(int pageType, String pageUrl) {
        String urlToLoad = "";
        switch (pageType) {
            case PageTypes.ABOUT:
                txt_title.setText(getString(R.string.about));
                urlToLoad = APIConstants.STATIC_PAGES.ABOUT_URL;
                getStaticPages(APIConstants.Params.ABOUT);
                scrollView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                break;

            case PageTypes.TERMS:
                txt_title.setText(getString(R.string.terms_conditions));
                urlToLoad = APIConstants.STATIC_PAGES.TERMS_URL;
                getStaticPages(APIConstants.Params.TERMS);
                scrollView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                break;
            case PageTypes.HELP:
                txt_title.setText(getString(R.string.help));
                urlToLoad = APIConstants.STATIC_PAGES.HELP_URL;
                getStaticPages(APIConstants.Params.HELP);
                scrollView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                break;

            case PageTypes.PRIVACY:
                txt_title.setText(getString(R.string.privacy_policy));
                urlToLoad = APIConstants.STATIC_PAGES.PRIVACY_URL;
                getStaticPages(APIConstants.Params.PRIVACY_POLICY);
                scrollView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                break;

            case PageTypes.SPEEDTEST:
                txt_title.setText(getString(R.string.speed_test));
                urlToLoad = APIConstants.STATIC_PAGES.SPEED_TEST_URL;
                scrollView.setVisibility(View.GONE);
                break;

            case PageTypes.REFUND:
                txt_title.setText(getString(R.string.refund_policy));
                urlToLoad = APIConstants.STATIC_PAGES.REFUND_URL;
                getStaticPages(APIConstants.Params.REFUND_POLICY);
                scrollView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                break;
            case PageTypes.BANNER:
                txt_title.setText("Banner title");
                urlToLoad = pageUrl;
                //urlToLoad = "http://www.google.com";
                //getStaticPages(APIConstants.Params.REFUND_POLICY);
                scrollView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(urlToLoad);
                break;
        }
//        webView.loadUrl(urlToLoad);
    }


    public static final class PageTypes {
        public static final int ABOUT = 0;
        public static final int TERMS = 1;
        public static final int HELP = 2;
        public static final int PRIVACY = 3;
        public static final int SPEEDTEST = 4;
        public static final int REFUND = 5;
        public static final int BANNER = 6;
    }

    private void getStaticPages(String pageType) {
        UiUtils.showLoadingDialog(this);
        String url = APIConstants.APIs.GET_STATIC_PAGE + "?"+ APIConstants.Params.PAGE_TYPE+ "=" + pageType;
        UiUtils.log(TAG,"Url: "+ url);
        Call<String> call = apiInterface.getStaticPage(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject staticPagesResponse = null;
                try {
                    staticPagesResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                try {
                    if (staticPagesResponse != null) {
                        if (staticPagesResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                            JSONObject data = staticPagesResponse.optJSONObject(APIConstants.Params.DATA);
                            staticText.setText(Html.fromHtml(data.optString(APIConstants.Params.DESCRIPTION)));
                        } else {
                            UiUtils.showShortToast(WebViewActivity.this, staticPagesResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                        }
                    }
                }
                catch (Exception e)
                {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(WebViewActivity.this);
            }
        });
    }
}
