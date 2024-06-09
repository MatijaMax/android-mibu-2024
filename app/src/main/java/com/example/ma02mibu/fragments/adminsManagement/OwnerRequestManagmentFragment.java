package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.adapters.adminsManagment.CategoryListAdapter;
import com.example.ma02mibu.adapters.adminsManagment.OwnerRequestListAdapter;
import com.example.ma02mibu.model.OwnerRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;

public class OwnerRequestManagmentFragment extends Fragment {

    private ListView ownerRequestListView;
    private OwnerRequestListAdapter adapter;
    private ArrayList<OwnerRequest> requests;
    private ArrayList<OwnerRequest> backupRequests;

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
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.OwnerRequestSheet);
            View dialogView = getLayoutInflater().inflate(R.layout.owner_request_sheet, null);
            bottomSheetDialog.setContentView(dialogView);
            Button submitSearchBtn = bottomSheetDialog.findViewById(R.id.searchButton);
            submitSearchBtn.setOnClickListener(f -> {
                EditText OwnerName = bottomSheetDialog.findViewById(R.id.OwnerNameEditText);
                EditText OwnerSurname = bottomSheetDialog.findViewById(R.id.OwnerSurnameEditText);
                EditText OwnerEmail = bottomSheetDialog.findViewById(R.id.OwnerEmailEditText);
                EditText CompanyEmail = bottomSheetDialog.findViewById(R.id.CompanyEmailEditText);
                EditText CompanyName = bottomSheetDialog.findViewById(R.id.CompanyNameEditText);
                String OwnerNameSearch = OwnerName.getText().toString();
                String OwnerSurnameSearch = OwnerSurname.getText().toString();
                String OwnerEmailSearch = OwnerEmail.getText().toString();
                String CompanyEmailSearch = CompanyEmail.getText().toString();
                String CompanyNameSearch = CompanyName.getText().toString();
                searchRequests(OwnerNameSearch, OwnerSurnameSearch, OwnerEmailSearch, CompanyEmailSearch, CompanyNameSearch);
                bottomSheetDialog.dismiss();
            });
            Button resetBtn = bottomSheetDialog.findViewById(R.id.resetButton);
            resetBtn.setOnClickListener(f -> {
                resetRequests();
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.findViewById(R.id.sortByDateButton).setOnClickListener(v1 -> {
                requests.sort(Comparator.comparing(OwnerRequest::getCreated));
                adapter = new OwnerRequestListAdapter(getContext(), requests);
                ownerRequestListView.setAdapter(adapter);
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.findViewById(R.id.sortByTypeButton).setOnClickListener(v1 -> {
                requests.sort((o1, o2) -> o1.getOwner().getEventTypes().get(0).compareTo(o2.getOwner().getEventTypes().get(0)));
                adapter = new OwnerRequestListAdapter(getContext(), requests);
                ownerRequestListView.setAdapter(adapter);
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.findViewById(R.id.sortByCategoryButton).setOnClickListener(v1 -> {
                requests.sort((o1, o2) -> o1.getOwner().getCategories().get(0).compareTo(o2.getOwner().getCategories().get(0)));
                adapter = new OwnerRequestListAdapter(getContext(), requests);
                ownerRequestListView.setAdapter(adapter);
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.show();
        });
    }

    private void searchRequests(String ownerNameSearch, String ownerSurnameSearch, String ownerEmailSearch, String companyEmailSearch, String companyNameSearch) {
        requests = new ArrayList<>(backupRequests);
        if (!ownerNameSearch.isEmpty())
            requests.removeIf(p -> !p.getOwner().getName().toLowerCase().contains(ownerNameSearch.toLowerCase()));
        if (!ownerSurnameSearch.isEmpty())
            requests.removeIf(p -> !p.getOwner().getSurname().toLowerCase().contains(ownerSurnameSearch.toLowerCase()));
        if (!ownerEmailSearch.isEmpty())
            requests.removeIf(p -> !p.getOwner().getEmail().toLowerCase().contains(ownerEmailSearch.toLowerCase()));
        if (!companyNameSearch.isEmpty())
            requests.removeIf(p -> !p.getOwner().getMyCompany().getName().toLowerCase().contains(companyEmailSearch.toLowerCase()));
        adapter = new OwnerRequestListAdapter(getContext(), requests);
        ownerRequestListView.setAdapter(adapter);
    }

    private void resetRequests() {
        requests = new ArrayList<>(backupRequests);
        adapter = new OwnerRequestListAdapter(getContext(), requests);
        ownerRequestListView.setAdapter(adapter);
    }

    private void getRequests() {
        CloudStoreUtil.selectOwnerRequest(result -> {
            backupRequests = new ArrayList<>(result);
            requests = result;
            adapter = new OwnerRequestListAdapter(getActivity(), requests);
            ownerRequestListView.setAdapter(adapter);
        });
    }
}