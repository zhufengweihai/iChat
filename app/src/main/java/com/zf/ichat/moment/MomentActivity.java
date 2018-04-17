package com.zf.ichat.moment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zf.ichat.R;
import com.zf.ichat.widget.RecycleViewDivider;

import java.io.FileNotFoundException;
import java.io.FileReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MomentActivity extends AppCompatActivity {
    @BindView(R.id.momentListView)
    RecyclerView momentListView;
    @BindView(R.id.bgView)
    ImageView bgView;
    @BindView(R.id.avatarView)
    ImageView avatarView;
    @BindView(R.id.nameView)
    TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_moment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_moment_back);

        try {
            String storageDir = Environment.getExternalStorageDirectory().toString() + "/Download/";
            FileReader fileReader = new FileReader(storageDir + "json.txt");
            User self = new Gson().fromJson(fileReader, User.class);
            Drawable drawable = BitmapDrawable.createFromPath(storageDir + self.getBackground());
            bgView.setBackground(drawable);
            Drawable avatar = BitmapDrawable.createFromPath(storageDir + self.getAvatar());
            avatarView.setImageDrawable(avatar);
            nameView.getPaint().setFakeBoldText(true);
            nameView.setText(self.getName());

            momentListView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
            momentListView.setLayoutManager(new LinearLayoutManager(this));
            momentListView.setAdapter(new MomentListAdapter(self.getMoments()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.moment, menu);
        return true;
    }
}
