package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.adminsManagment.SubcategoryRequestListAdapter;
import com.example.ma02mibu.model.SubcategoryProposal;

import java.util.ArrayList;

public class SubcategoryRequestReviewTabFragment extends Fragment {
    private ListView subcategoryRequestListView;
    private SubcategoryRequestListAdapter subcategoryRequestListAdapter;
    private ArrayList<SubcategoryProposal> subcategoryProposals = new ArrayList<>();
    public SubcategoryRequestReviewTabFragment() { }

    public static SubcategoryRequestReviewTabFragment newInstance() {
        SubcategoryRequestReviewTabFragment fragment = new SubcategoryRequestReviewTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subcategoryRequestListAdapter = new SubcategoryRequestListAdapter(getActivity(), subcategoryProposals);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subcategory_request_review_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        subcategoryRequestListView = view.findViewById(R.id.subCategoriesRequestsListView);
        getProposals();

        subcategoryRequestListView.setAdapter(subcategoryRequestListAdapter);
        subcategoryRequestListView.setOnItemClickListener((parent, view1, position, id) -> FragmentTransition.to(SubcategoryRequestReviewEditFragment.newInstance(subcategoryRequestListAdapter.getItem(position)),
                requireActivity(), true, R.id.categoryManagementContainer, "subcategoryRequestManagement"));
    }

    private void getProposals(){
        CloudStoreUtil.selectSubcategoryProposal(result -> {
            subcategoryProposals = result;
            subcategoryRequestListAdapter = new SubcategoryRequestListAdapter(getActivity(), subcategoryProposals);
            subcategoryRequestListView.setAdapter(subcategoryRequestListAdapter);
        });
    }
}