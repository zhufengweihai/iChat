package com.zf.ichat.convr;

import android.support.annotation.NonNull;

import com.zf.ichat.BasePresenter;
import com.zf.ichat.BaseView;
import com.zf.ichat.data.Convr;

import java.util.List;

public interface ConversationContract {

    interface Presenter extends BasePresenter {
        void loadConversations(boolean forceUpdate);

        void addNewConversation();

        void talk(@NonNull Convr convr);

        void deleteConversation(Convr convr);

    }

    interface View extends BaseView {
        void showConversations(List<Convr> convrs);
    }
}