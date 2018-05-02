package com.zf.ichat.contact;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

import com.zf.ichat.data.Contact;

public class ContactAdapter extends PagedListAdapter<Contact, ContactViewHolder> {
    ContactAdapter() {
        super(getDiffCallback());
    }

    @NonNull
    private static DiffUtil.ItemCallback<Contact> getDiffCallback() {
        return new DiffUtil.ItemCallback<Contact>() {
            @Override
            public boolean areItemsTheSame(Contact oldItem, Contact newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(Contact oldItem, Contact newItem) {
                return oldItem == newItem;
            }
        };
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }
}