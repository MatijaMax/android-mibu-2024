package com.example.ma02mibu.fragments.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.MainActivity;
import com.example.ma02mibu.adapters.authentication.CategoryListAdapter;
import com.example.ma02mibu.adapters.authentication.EventTypeListAdapter;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.EventType;

import java.util.ArrayList;

public class SignUpPUP4Fragment extends Fragment {

    private ArrayList<Category> mCategories;
    private CategoryListAdapter mCategoriesAdapter;
    private ArrayList<EventType> mEventTypes;
    private EventTypeListAdapter mEventTypesAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categories List of all the categories.
     * @param eventTypes List of all the event types.
     * @return A new instance of fragment SignUpPUP4Fragment.
     */
    public static SignUpPUP4Fragment newInstance(ArrayList<Category> categories, ArrayList<EventType> eventTypes) {
        SignUpPUP4Fragment fragment = new SignUpPUP4Fragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, categories);
        args.putParcelableArrayList(ARG_PARAM2, eventTypes);
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpPUP4Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategories = getArguments().getParcelableArrayList(ARG_PARAM1);
            mCategoriesAdapter = new CategoryListAdapter(getActivity(), mCategories);
            mEventTypes = getArguments().getParcelableArrayList(ARG_PARAM2);
            mEventTypesAdapter = new EventTypeListAdapter(getActivity(), mEventTypes);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_p_u_p4, container, false);

        ListView categoryListView = view.findViewById(R.id.categoryListView);
        ListView eventTypeListView = view.findViewById(R.id.eventTypeListView);
        categoryListView.setAdapter(mCategoriesAdapter);
        eventTypeListView.setAdapter(mEventTypesAdapter);

        Button btnLogin = view.findViewById(R.id.btnSignUpPUP);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}