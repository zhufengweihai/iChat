package com.zf.ichat.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup.LayoutParams;

import com.zf.ichat.BaseActivity;
import com.zf.ichat.R;
import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Contact;
import com.zf.ichat.data.Convr;
import com.zf.ichat.data.Message;
import com.zf.ichat.data.MessageType;
import com.zf.ichat.widget.DMTabHost;
import com.zf.ichat.widget.TitlePopup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tab_host)
    DMTabHost host;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private TitlePopup titlePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPopWindow();

        ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getOwner().observe(this, owner -> {
            // 更新 UI
        });

        AsyncTask.execute(() -> {
            ChatDao chatDao = ChatDatabase.instance(this).chatDao();
            Contact[] contacts = new Contact[10];
            for (int i = 0; i < 10; i++) {
                Contact contact = new Contact();
                contact.setId((short) i);
                contact.setAvatarUrl("https://imgsa.baidu" + "" + "" + "" +
                        ".com/baike/pic/item/d01373f082025aaf192b6064f3edab64034f1a07.jpg");
                String s = "zf" + i;
                contact.setUserName(s);
                contact.setNickname(s);
                contacts[i] = contact;
            }
            chatDao.insertContacts(contacts);

            Convr[] convrs = new Convr[10];
            for (int i = 0; i < 10; i++) {
                Convr convr = new Convr();
                convr.setContactId((short) i);
                convrs[i] = convr;
            }
            chatDao.insertConvrs(convrs);

            Message[] messages = new Message[10];
            for (int i = 0; i < 10; i++) {
                Message message = new Message();
                message.setContactId((short) 0);
                message.setCreateTime(i);
                boolean b = i % 2 == 0;
                message.setBelong(b);
                message.setMessage(b ? "那些让人过目不忘的照片，最后一张满满的即视感。" : "http://img.ivsky" + "" + "" +
                        ".com/img/tupian/slides/201803/15/fangsuo_shudian.jpg");
                message.setType(b ? MessageType.Text : MessageType.Image);
                messages[i] = message;
            }
            chatDao.insertMessages(messages);
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        host.setChecked(0);
        host.setHasNew(2, true);
    }
}
