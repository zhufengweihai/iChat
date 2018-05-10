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
import android.widget.TextView;

import com.zf.ichat.R;
import com.zf.ichat.widget.DividerItemDecoration;
import com.zf.ichat.widget.IndexBar;
import com.zf.ichat.widget.SuspensionDecoration;

/**
 * @author zhufeng
 */
public class ContactFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);
        RecyclerView contactView = view.findViewById(R.id.contactView);
        contactView.addItemDecoration(new DividerItemDecoration(inflater.getContext(), LinearLayoutManager.HORIZONTAL));
        contactView.addItemDecoration(new SuspensionDecoration(inflater.getContext()));
        ContactAdapter adapter = new ContactAdapter();
        contactView.setAdapter(adapter);
        ContactViewModel viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        viewModel.getContacts().observe(this, adapter::submitList);

        TextView indexView = view.findViewById(R.id.indexView);
        IndexBar indexBar = view.findViewById(R.id.indexBar);
        indexBar.setOnIndexPressedListener(new IndexBar.onIndexPressedListener() {
            @Override
            public void onIndexPressed(int index, String text) {
                indexView.setText(text);
                indexView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMotionEventEnd() {
                indexView.setVisibility(View.GONE);
                viewModel.getPosition(indexView.getText().toString()).observe(ContactFragment.this, pos -> (
                        (LinearLayoutManager) contactView.getLayoutManager()).scrollToPositionWithOffset(pos, 0));
            }
        });

        return view;
    }
}