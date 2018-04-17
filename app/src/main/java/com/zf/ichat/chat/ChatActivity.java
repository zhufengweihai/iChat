package com.zf.ichat.chat;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zf.ichat.R;
import com.zf.ichat.common.Constant;
import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Contact;
import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;
import com.zf.ichat.data.MessageType;
import com.zf.ichat.main.ContactViewModel;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class ChatActivity extends AppCompatActivity {
    private LinearLayoutManager layoutManager;
    private AsyncListUtil<Message> asyncListUtil;
    private MessageListAdapter adapter;
    private Conversation conversation;
    private Contact owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        RecyclerView chatView = findViewById(R.id.chatView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        chatView.setLayoutManager(layoutManager);
        chatView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                chatView.postDelayed(() -> chatView.scrollToPosition(0), 100);
            }
        });
        asyncListUtil = new AsyncListUtil<>(Message.class, Constant.CACHE_SIZE, new DataCallback(), new ViewCallback());
        chatView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                asyncListUtil.onRangeChanged();
            }
        });

        conversation = (Conversation) getIntent().getSerializableExtra("conversation");
        ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getOwner().observe(this, owner -> {
            this.owner = owner;
            getSupportActionBar().setTitle(owner.getNickname());
            adapter = new MessageListAdapter();
            chatView.setAdapter(adapter);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    private class ViewCallback extends AsyncListUtil.ViewCallback {
        @Override
        public void getItemRangeInto(int[] outRange) {
            outRange[0] = Math.max(0, layoutManager.findFirstVisibleItemPosition());
            outRange[1] = Math.max(0, layoutManager.findLastVisibleItemPosition());
        }

        @Override
        public void onDataRefresh() {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemLoaded(int position) {
            adapter.notifyItemChanged(position);
        }
    }

    private class DataCallback extends AsyncListUtil.DataCallback<Message> {

        @Override
        public int refreshData() {
            return ChatDatabase.instance(ChatActivity.this).chatDao().getConvrCount();
        }

        @Override
        public void fillData(Message[] data, int startPosition, int itemCount) {
            ChatDao chatDao = ChatDatabase.instance(ChatActivity.this).chatDao();
            List<Message> list = chatDao.getMessages(conversation.getContactId(), itemCount, startPosition);
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }
        }
    }

    private class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public int getItemViewType(int position) {
            Message message = asyncListUtil.getItem(position);
            if (message != null) {
                return message.getType().ordinal();
            }
            return super.getItemViewType(position);
        }

        @Override
        @NonNull
        public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == MessageType.Image.ordinal()) {
                View view = inflater.inflate(R.layout.item_chat_image, parent, false);
                return new TextViewHolder(view);
            }

            View view = inflater.inflate(R.layout.item_chat_text, parent, false);
            return new TextViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Message message = asyncListUtil.getItem(position);
            if (message != null) {
                if (holder instanceof TextViewHolder) {
                    TextViewHolder textHolder = (TextViewHolder) holder;
                    int pL = textHolder.messageView.getPaddingLeft();
                    int pT = textHolder.messageView.getPaddingTop();
                    int pR = textHolder.messageView.getPaddingRight();
                    int pB = textHolder.messageView.getPaddingBottom();
                    if (message.isBelong()) {
                        Glide.with(textHolder.avatarView).load(conversation.getAvatarUrl()).into(textHolder.avatarView);
                        textHolder.messageView.setBackgroundResource(R.drawable.selector_bg_message);
                        textHolder.itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    } else {
                        Glide.with(textHolder.avatarView).load(owner.getAvatarUrl()).into(textHolder.avatarView);
                        textHolder.messageView.setBackgroundResource(R.drawable.selector_bg_my_message);
                        textHolder.itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    }
                    textHolder.messageView.setPadding(pL, pT, pR, pB);
                    textHolder.messageView.setText(message.getMessage());
                } else if (holder instanceof ImageViewHolder) {
                    ImageViewHolder imageHolder = (ImageViewHolder) holder;
                    Glide.with(imageHolder.imageView).load(message.getMessage()).into(imageHolder.imageView);
                    if (message.isBelong()) {
                        Glide.with(imageHolder.avatarView).load(conversation.getAvatarUrl()).into(imageHolder
                                .avatarView);
                        imageHolder.itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    } else {
                        Glide.with(imageHolder.avatarView).load(owner.getAvatarUrl()).into(imageHolder.avatarView);
                        imageHolder.itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return asyncListUtil.getItemCount();
        }
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatarView;
        private final TextView messageView;

        private TextViewHolder(View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.avatarView);
            messageView = itemView.findViewById(R.id.messageView);
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatarView;
        private final PhotoView imageView;

        private ImageViewHolder(View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.avatarView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
