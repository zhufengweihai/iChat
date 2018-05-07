package com.zf.ichat.contact;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

import com.zf.ichat.data.Contact;
import com.zf.ichat.widget.IndexBar;

import java.util.Arrays;

public class ContactAdapter extends PagedListAdapter<Contact, ContactViewHolder> {
    private int index = -1;

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

    @Override
    public long getItemId(int position) {
        int index = getIndex(getItem(position).getPinyin());
        if (index != this.index) {
            this.index = index;
            return index;
        }
        return super.getItemId(position);
    }

    private int getIndex(String pinyin) {
        String initials = pinyin.substring(0, 1);
        int index = Arrays.binarySearch(IndexBar.INDEX_STRING, initials);
        return index >= 0 ? index : IndexBar.INDEX_STRING.length - 1;
    }

    public int getPosition(String initials) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            if (getItem(i).getPinyin().startsWith(initials)) {
                return i;
            }
        }
        return count - 1;
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