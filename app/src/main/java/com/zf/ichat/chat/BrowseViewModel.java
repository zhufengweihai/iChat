package com.zf.ichat.chat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Message;

import java.util.List;

/**
 * @author zhufeng
 */
public class BrowseViewModel extends AndroidViewModel {
    private ChatDao chatDao = ChatDatabase.instance(getApplication()).chatDao();

    private LiveData<List<Message>> messages;

    public BrowseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Message>> getImageMessages(short contactId) {
        if (messages == null) {
            messages = chatDao.getMessagesByType(Message.IMAGE, contactId);
        }
        return messages;
    }
}