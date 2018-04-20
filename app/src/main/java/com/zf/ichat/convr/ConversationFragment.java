package com.zf.ichat.convr;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zf.ichat.widget.RecycleViewDivider;

/**
 * @author zhufeng
 */
public class ConversationFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = inflater.getContext();
        RecyclerView conversationView = new RecyclerView(context);
        conversationView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL));
        conversationView.setLayoutManager(new LinearLayoutManager(context));

        ConversationAdapter adapter = new ConversationAdapter();
        conversationView.setAdapter(adapter);
        ConvrViewModel viewModel = ViewModelProviders.of(this).get(ConvrViewModel.class);
        viewModel.getConvrs().observe(this, adapter::submitList);

        return conversationView;
    }
}