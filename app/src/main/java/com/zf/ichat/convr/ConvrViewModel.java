package com.zf.ichat.convr;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Contact;
import com.zf.ichat.data.Conversation;

public class ConvrViewModel extends AndroidViewModel {
    private final static int PAGE_SIZE = 30;
    private ChatDao chatDao = ChatDatabase.instance(getApplication()).chatDao();

    private MutableLiveData<Contact> owner;
    private LiveData<PagedList<Conversation>> convrs;

    public ConvrViewModel(@NonNull Application application) {
        super(application);
        convrs = new LivePagedListBuilder<>(chatDao.getAllConvrs(), new PagedList.Config.Builder().setPageSize
                (PAGE_SIZE).setEnablePlaceholders(true).build()).build();
    }

    public LiveData<PagedList<Conversation>> getConvrs() {
        return convrs;
    }
}