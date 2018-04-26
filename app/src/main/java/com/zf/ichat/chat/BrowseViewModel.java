package com.zf.ichat.chat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PagedList.Config.Builder;
import android.support.annotation.NonNull;

import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Message;
import com.zf.ichat.data.MessageType;

/**
 * @author zhufeng
 */
public class BrowseViewModel extends AndroidViewModel {
    private final static int PAGE_SIZE = 30;
    private ChatDao chatDao = ChatDatabase.instance(getApplication()).chatDao();

    private LiveData<PagedList<Message>> messages;

    public BrowseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<PagedList<Message>> getImageMessages(short contactId) {
        if (messages == null) {
            PagedList.Config config = new Builder().setPageSize(PAGE_SIZE).setEnablePlaceholders(true).build();
            DataSource.Factory<Integer, Message> messages = chatDao.getMessagesByType(MessageType.Image, contactId);
            this.messages = new LivePagedListBuilder<>(messages, config).build();
        }
        return messages;
    }
}