package com.zf.ichat.convr;

import android.support.annotation.NonNull;

import com.zf.ichat.data.Convr;

public class ConversationPresenter implements ConversationContract.Presenter {
    @Override
    public void loadConversations(boolean forceUpdate) {

    }

    @Override
    public void addNewConversation() {

    }

    @Override
    public void talk(@NonNull Convr convr) {

    }

    @Override
    public void deleteConversation(Convr convr) {

    }

    @Override
    public void start() {
        loadConversations(false);
    }
}
