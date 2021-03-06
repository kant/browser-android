package com.cliqz.browser.main.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cliqz.browser.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Bind(R.id.wv_privacy_policy)
    WebView privacyPolicy;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        privacyPolicy.loadUrl(getString(R.string.privacy_policy_url));
        privacyPolicy.getSettings().setJavaScriptEnabled(true);
        privacyPolicy.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                privacyPolicy.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
