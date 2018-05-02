package com.zf.ichat.contact;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Contact;

public class ContactViewModel extends AndroidViewModel {
    private final static int PAGE_SIZE = 30;
    private ChatDao chatDao = ChatDatabase.instance(getApplication()).chatDao();

    private LiveData<PagedList<Contact>> convrs;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        convrs = new LivePagedListBuilder<>(chatDao.getAllContacts(), new PagedList.Config.Builder().setPageSize
                (PAGE_SIZE).setEnablePlaceholders(true).build()).build();
    }

    public LiveData<PagedList<Contact>> getConvrs() {
        return convrs;
    }
}