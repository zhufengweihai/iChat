package com.zf.ichat.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.zf.ichat.data.Contact;

public class ContactViewModel extends AndroidViewModel {
    private MutableLiveData<Contact> owner;

    public ContactViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contact> getOwner() {
        if (owner == null) {
            owner = new MutableLiveData<>();
            loadOwner();
        }
        return owner;
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