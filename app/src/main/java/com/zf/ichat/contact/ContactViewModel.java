package com.zf.ichat.contact;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Contact;

public class ContactViewModel extends AndroidViewModel {
    private final static int PAGE_SIZE = 30;
    private ChatDao chatDao = ChatDatabase.instance(getApplication()).chatDao();

    private LiveData<PagedList<Contact>> contacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<PagedList<Contact>> getContacts() {
        if (contacts == null) {
            contacts = new LivePagedListBuilder<>(new DataSource.Factory<Integer, Contact>() {
                @Override
                public DataSource<Integer, Contact> create() {
                    return new WrapPositionalDataSource(chatDao);
                }
            }, new PagedList.Config.Builder().setPageSize(PAGE_SIZE).setEnablePlaceholders(true).build()).build();
        }
        return contacts;
    }
}