package com.zf.ichat.chat;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.zf.ichat.BaseActivity;
import com.zf.ichat.R;

public class ImageBrowseActivity extends BaseActivity {
    public static final String INTENT = "contactId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);
    }

    @Override
    protected void initView() {
        ViewPager browseView = findViewById(R.id.browseView);
        BrowseViewModel viewModel = ViewModelProviders.of(this).get(BrowseViewModel.class);

        short contactId = (Short) getIntent().getSerializableExtra(INTENT);
        ImageBrowseAdapter adapter = new ImageBrowseAdapter();
        browseView.setAdapter(adapter);
        viewModel.getImageMessages(contactId).observe(this, adapter::setData);
    }
}
