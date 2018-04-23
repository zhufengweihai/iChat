package com.zf.ichat.chat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PagedList.Config.Builder;
import android.support.annotation.NonNull;

import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Conversation;
import com.zf.ichat.data.Message;

/**
 * @author zhufeng
 */
public class ChatViewModel extends AndroidViewModel {
    private final static int PAGE_SIZE = 30;
    private ChatDao chatDao = ChatDatabase.instance(getApplication()).chatDao();

    private LiveData<PagedList<Message>> messages;

    public ChatViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<PagedList<Message>> getMessages(short contactId) {
        if (messages == null) {
            messages = new LivePagedListBuilder<>(chatDao.getMessages(contactId), new Builder().setPageSize(PAGE_SIZE).setEnablePlaceholders(true).build()).build();
        }
        return messages;
    }
}