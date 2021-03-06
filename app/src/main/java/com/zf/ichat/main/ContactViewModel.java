package com.zf.ichat.main;

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

public class ContactViewModel extends AndroidViewModel {
    private final static int PAGE_SIZE = 30;
    private ChatDao chatDao = ChatDatabase.instance(getApplication()).chatDao();

    private MutableLiveData<Contact> owner;
    private LiveData<PagedList<Conversation>> convrs;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        convrs = new LivePagedListBuilder<>(chatDao.getAllConvrs(), new PagedList.Config.Builder().setPageSize
                (PAGE_SIZE).setEnablePlaceholders(true).build()).build();
    }

    public LiveData<Contact> getOwner() {
        if (owner == null) {
            owner = new MutableLiveData<>();
            loadOwner();
        }
        return owner;
    }

    public LiveData<PagedList<Conversation>> getConvrs() {
        return convrs;
    }

    private void loadOwner() {
        Contact contact = new Contact();
        contact.setId((short) -1);
        contact.setNickname("稻草人");
        contact.setUserName("zf");
        contact.setAvatarUrl("https://imgsa.baidu.com/baike/pic/item/d009b3de9c82d15893832d7d880a19d8bd3e42fc.jpg");
        owner.setValue(contact);
    }


}