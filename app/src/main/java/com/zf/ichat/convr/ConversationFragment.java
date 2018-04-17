package com.zf.ichat.convr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zf.ichat.R;
import com.zf.ichat.chat.ChatActivity;
import com.zf.ichat.common.Constant;
import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Convr;
import com.zf.ichat.widget.RecycleViewDivider;

import java.util.List;


/**
 * @author zhufeng
 */
public class ConversationFragment extends Fragment implements ConversationContract.View {
    private LinearLayoutManager layoutManager;
    private AsyncListUtil<Conversation> asyncListUtil;
    private ConversationListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = inflater.getContext();
        RecyclerView conversationView = new RecyclerView(context);
        conversationView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL));
        layoutManager = new LinearLayoutManager(context);
        conversationView.setLayoutManager(layoutManager);
        adapter = new ConversationListAdapter();
        conversationView.setAdapter(adapter);
        asyncListUtil = new AsyncListUtil<>(Conversation.class, Constant.CACHE_SIZE, new DataCallback(), new
                ViewCallback());
        conversationView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                asyncListUtil.onRangeChanged();
            }
        });

        return conversationView;
    }

    @Override
    public void showConversations(List<Convr> convrs) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String msg) {

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

    private class DataCallback extends AsyncListUtil.DataCallback<Conversation> {

        @Override
        public int refreshData() {
            return ChatDatabase.instance(getContext()).chatDao().getConvrCount();
        }

        @Override
        public void fillData(Conversation[] data, int startPosition, int itemCount) {
            ChatDao chatDao = ChatDatabase.instance(getContext()).chatDao();
            List<Conversation> list = chatDao.getConvrsAfter(itemCount, startPosition);
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }
        }
    }

    private class ConversationListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        @Override
        @NonNull
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_convr_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Conversation convr = asyncListUtil.getItem(position);
            if (convr != null) {
                Glide.with(holder.avatarView).load(convr.getAvatarUrl()).into(holder.avatarView);
                holder.contentView.setText(convr.getMessage());
                holder.nameView.setText(convr.getNickname());
                holder.timeView.setText(String.valueOf(convr.getCreateTime()));
                holder.unreadView.setText(String.valueOf(convr.getUnread()));
                holder.itemView.setOnClickListener(v -> {
                    Intent startIntent = new Intent(getContext(), ChatActivity.class);
                    startIntent.putExtra("conversation", convr);
                    startActivity(startIntent);
                });
            }
        }

        @Override
        public int getItemCount() {
            return asyncListUtil.getItemCount();
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatarView;
        private final TextView unreadView;
        private final TextView nameView;
        private final TextView contentView;
        private final TextView timeView;

        private ItemViewHolder(View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.avatarView);
            unreadView = itemView.findViewById(R.id.unreadView);
            nameView = itemView.findViewById(R.id.nameView);
            contentView = itemView.findViewById(R.id.contentView);
            timeView = itemView.findViewById(R.id.timeView);
        }
    }
}