package com.zf.ichat.contact;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zf.ichat.R;
import com.zf.ichat.widget.RecycleViewDivider;

/**
 * @author zhufeng
 */
public class ContactFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);
        RecyclerView contactView = view.findViewById(R.id.contactView);
        contactView.addItemDecoration(new RecycleViewDivider(inflater.getContext(), LinearLayoutManager.HORIZONTAL));
        ContactAdapter adapter = new ContactAdapter();
        contactView.setAdapter(adapter);
        ContactViewModel viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        viewModel.getConvrs().observe(this, adapter::submitList);
        return view;
    }
}