package com.example.cityguide.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.example.cityguide.R;

public class ChatBot extends AppCompatActivity {

    private static final String CHAT_BOT_URL = "https://console.dialogflow.com/api-client/demo/embedded/d6b641d4-ec87-4902-84cc-829a563421fd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        WebView webView = findViewById(R.id.chat_bot_web_view);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(CHAT_BOT_URL);

        findViewById(R.id.back_image_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}