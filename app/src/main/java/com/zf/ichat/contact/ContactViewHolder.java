package com.zf.ichat.contact;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zf.ichat.R;
import com.zf.ichat.data.Contact;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    public ContactViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
    }

    public void bindTo(Contact contact) {
        Glide.with(itemView).load(contact.getAvatarUrl()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                ((TextView) itemView).setCompoundDrawablesRelative(resource, null, null, null);
            }
        });
        ((TextView) itemView).setText(contact.getNickname());
    }
}