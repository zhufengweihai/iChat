package com.zf.ichat.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zf.ichat.ChatApplication;
import com.zf.ichat.R;
import com.zf.ichat.data.Contact;
import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;
import com.zf.ichat.util.DateUtils;
import com.zf.ichat.util.ViewHolder;

import java.text.DateFormat;
import java.util.Date;

public abstract class MessageViewHolder extends ViewHolder<Message> {
    private DateFormat todayFormat;
    private DateFormat dateFormat;
    private final TextView timeView;
    final ImageView avatarView;
    Contact self;
    Conversation convr;

    public MessageViewHolder(View view, Conversation convr) {
        super(view);
        timeView = itemView.findViewById(R.id.timeView);
        avatarView = itemView.findViewById(R.id.avatarView);
        self = ((ChatApplication) itemView.getContext().getApplicationContext()).getSelf();
        this.convr = convr;
        todayFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    }

    @Override
    public void bindTo(Message message) {
        long createTime = message.getCreateTime();
        if (System.currentTimeMillis() - createTime >= 60 * 1000) {
            Date date = new Date(createTime);
            if (DateUtils.isToday(createTime)) {
                timeView.setText(todayFormat.format(date));
            } else if (DateUtils.isYesterday(createTime)) {
                timeView.setText(timeView.getContext().getString(R.string.format_yesterday, todayFormat.format(date)));
            } else {
                timeView.setText(dateFormat.format(date));
            }
            timeView.setVisibility(View.VISIBLE);
        } else {
            timeView.setVisibility(View.GONE);
        }
    }
}