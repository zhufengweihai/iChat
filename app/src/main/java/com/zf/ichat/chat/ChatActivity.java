package com.zf.ichat.chat;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.zf.ichat.BaseActivity;
import com.zf.ichat.R;

public class ChatActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        RecyclerView chatView = findViewById(R.id.chatView);
        chatView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                chatView.postDelayed(() -> chatView.scrollToPosition(0), 100);
            }
        });


        ChatViewModel viewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        viewModel.getCurrentConvr().observe(this, convr -> {
            MessageAdapter adapter = new MessageAdapter(convr);
            chatView.setAdapter(adapter);
            viewModel.getMessages().observe(this, adapter::submitList);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }
}
