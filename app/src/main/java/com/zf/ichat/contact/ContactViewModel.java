package com.zf.ichat.contact;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import com.zf.ichat.data.ChatDao;
import com.zf.ichat.data.ChatDatabase;
import com.zf.ichat.data.Contact;

import java.util.Collections;
import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private final static int PAGE_SIZE = 30;
    private ChatDao chatDao = ChatDatabase.instance(getApplication()).chatDao();

    private LiveData<PagedList<Contact>> contacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        contacts = new LivePagedListBuilder<>(new DataSource.Factory<Integer, Contact>() {
            @Override
            public DataSource<Integer, Contact> create() {
                return new PositionalDataSource<Contact>() {
                    @Override
                    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Contact>
                            callback) {
                        int totalCount = chatDao.getContactCount();
                        if (totalCount == 0) {
                            callback.onResult(Collections.emptyList(), 0, 0);
                            return;
                        }

                        final int firstLoadPosition = computeInitialLoadPosition(params, totalCount);
                        final int firstLoadSize = computeInitialLoadSize(params, firstLoadPosition, totalCount);

                        List<Contact> list = loadRange(firstLoadPosition, firstLoadSize);
                        if (list != null && list.size() == firstLoadSize) {
                            callback.onResult(list, firstLoadPosition, totalCount);
                        } else {
                            invalidate();
                        }
                    }

                    @Override
                    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Contact>
                            callback) {
                        List<Contact> list = loadRange(params.startPosition, params.loadSize);
                        if (list != null) {
                            callback.onResult(list);
                        } else {
                            invalidate();
                        }
                    }

                    public List<Contact> loadRange(int startPosition, int loadCount) {
                        return chatDao.getContacts(startPosition, loadCount);
                    }
                };
            }
        }, new PagedList.Config.Builder().setPageSize(PAGE_SIZE).setEnablePlaceholders(true).build()).build();
    }

    public LiveData<PagedList<Contact>> getContacts() {
        return contacts;
    }
}