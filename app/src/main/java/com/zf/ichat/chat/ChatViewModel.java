package com.zf.ichat.chat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;

public class ChatViewModel extends AndroidViewModel {
    private final static int PAGE_SIZE = 30;
    private ChatDao chatDao = ChatDatabase.instance(getApplication()).chatDao();

    private MutableLiveData<Conversation> currentConvr;
    private LiveData<PagedList<Message>> messages;

    public ChatViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Conversation> getCurrentConvr() {
        return currentConvr;
    }

    public void setCurrentConvr(Conversation currentConvr) {
        this.currentConvr.setValue(currentConvr);
        messages = new LivePagedListBuilder<>(chatDao.getMessages(currentConvr.getContactId()), new PagedList.Config
                .Builder().setPageSize(PAGE_SIZE).setEnablePlaceholders(true).build()).build();

    }

    public LiveData<PagedList<Message>> getMessages() {
        return messages;
    }
}