package com.zf.ichat.contact;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zf.ichat.R;
import com.zf.ichat.widget.DividerItemDecoration;

import static android.widget.RadioGroup.LayoutParams;

/**
 * @author zhufeng
 */
public class ContactFragment extends Fragment {
    private static final String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);
        RecyclerView contactView = view.findViewById(R.id.contactView);
        contactView.addItemDecoration(new DividerItemDecoration(inflater.getContext(), LinearLayoutManager.HORIZONTAL));
        ContactAdapter adapter = new ContactAdapter();
        contactView.setAdapter(adapter);
        ContactViewModel viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        viewModel.getContacts().observe(this, adapter::submitList);

        RadioGroup indexGroup = view.findViewById(R.id.indexGroup);
        for (String letter : LETTERS) {
            RadioButton button = new RadioButton(getContext());
            button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            button.setText(letter);
            button.setButtonDrawable(null);
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
            indexGroup.addView(button);
        }

        return view;
    }
}