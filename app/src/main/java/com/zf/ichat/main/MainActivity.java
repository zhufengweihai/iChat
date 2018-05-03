package com.zf.ichat.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup.LayoutParams;

import com.zf.ichat.BaseActivity;
import com.zf.ichat.ChatApplication;
import com.zf.ichat.R;
import com.zf.ichat.widget.PagerTabGroup;
import com.zf.ichat.widget.TitlePopup;

import butterknife.BindView;

/**
 * @author zhufeng
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.tabGroup)
    PagerTabGroup tabGroup;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private TitlePopup titlePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPopWindow();

        ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getOwner().observe(this, owner -> {
            ((ChatApplication) getApplication()).setSelf(owner);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            titlePopup.show(findViewById(R.id.action_add));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initPopWindow() {
        titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titlePopup.addAction(new TitlePopup.ActionItem(this, R.string.menu_groupchat, R.drawable.icon_menu_group));
        titlePopup.addAction(new TitlePopup.ActionItem(this, R.string.menu_addfriend, R.drawable.icon_menu_addfriend));
        titlePopup.addAction(new TitlePopup.ActionItem(this, R.string.menu_qrcode, R.drawable.icon_menu_scan));
        titlePopup.setItemOnClickListener((item, position) -> {
            switch (position) {
                case 0:// 发起群聊
                    break;
                case 1:// 添加朋友
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tabGroup.setChecked(0);
        tabGroup.setHasNew(2, true);
        tabGroup.setViewPager(viewpager);
    }
}
