package com.zf.ichat.contact;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

public class WrapPositionalDataSource<T> extends PositionalDataSource<T> {
    private final PositionalDataSource<T> source;

    public WrapPositionalDataSource(PositionalDataSource<T> source) {
        this.source = source;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<T> callback) {
        source.loadInitial(params, callback);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<T> callback) {
        source.loadRange(params, callback);
    }
}
