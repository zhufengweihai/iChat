package com.zf.ichat.contact;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.Contact;

import java.util.Collections;
import java.util.List;

public class WrapPositionalDataSource extends PositionalDataSource<Contact> {
    private ChatDao chatDao;
    private String initials=" ";

    public WrapPositionalDataSource(ChatDao chatDao) {
        this.chatDao = chatDao;
    }

    @Override
    public void loadInitial(@NonNull PositionalDataSource.LoadInitialParams params, @NonNull
            LoadInitialCallback<Contact> callback) {
        int totalCount = chatDao.getContactCount();
        if (totalCount == 0) {
            callback.onResult(Collections.emptyList(), 0, 0);
            return;
        }

        final int firstLoadPosition = computeInitialLoadPosition(params, totalCount);
        final int firstLoadSize = computeInitialLoadSize(params, firstLoadPosition, totalCount);

        List<Contact> list = loadRange(firstLoadPosition, firstLoadSize);
        if (list != null && list.size() == firstLoadSize) {
            callback.onResult(list, firstLoadPosition, totalCount);
        } else {
            invalidate();
        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Contact> callback) {
        List<Contact> list = loadRange(params.startPosition, params.loadSize);
        if (list != null) {
            callback.onResult(list);
        } else {
            invalidate();
        }
    }

    private List<Contact> loadRange(int startPosition, int loadCount) {
        List<Contact> contacts = chatDao.getContacts(loadCount, startPosition);
        if (contacts.size() > 0) {
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                if (!contact.getPinyin().startsWith(initials)) {
                    initials = contact.getPinyin().substring(0, 1);
                    contacts.set(i, new WrapContact(contact, initials));
                }
            }
        }
        return contacts;
    }
}
