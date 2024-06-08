package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.adminsManagment.CategoryListAdapter;
import com.example.ma02mibu.adapters.adminsManagment.OwnerRequestListAdapter;
import com.example.ma02mibu.model.OwnerRequest;

import java.util.ArrayList;

public class OwnerRequestManagmentFragment extends Fragment {

    private ListView ownerRequestListView;
    private OwnerRequestListAdapter adapter;
    private ArrayList<OwnerRequest> requests;
    public OwnerRequestManagmentFragment() { }

    public static OwnerRequestManagmentFragment newInstance() {
        OwnerRequestManagmentFragment fragment = new OwnerRequestManagmentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_owner_request_managment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ownerRequestListView = view.findViewById(R.id.ownerRequestListView);
        getRequests();

        ownerRequestListView.setOnItemClickListener((parent, view1, position, id) -> {
            //TODO open details
        });

        view.findViewById(R.id.filterRequests).setOnClickListener(v -> {
            //TODO open filter menu
        });
    }

    private void getRequests() {
        CloudStoreUtil.selectOwnerRequest(result -> {
            requests = result;
            adapter = new OwnerRequestListAdapter(getActivity(), requests);
            ownerRequestListView.setAdapter(adapter);
        });
    }
}