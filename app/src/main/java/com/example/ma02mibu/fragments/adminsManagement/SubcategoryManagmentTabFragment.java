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
import com.example.ma02mibu.adapters.adminsManagment.SubcategoryListAdapter;
import com.example.ma02mibu.model.Subcategory;

import java.util.ArrayList;

public class SubcategoryManagmentTabFragment extends Fragment {

    private ListView subcategoryListView;
    private SubcategoryListAdapter subcategoryListAdapter;
    private ArrayList<Subcategory> subcategories = new ArrayList<>();
    public SubcategoryManagmentTabFragment() { }

    public static SubcategoryManagmentTabFragment newInstance() {
        SubcategoryManagmentTabFragment fragment = new SubcategoryManagmentTabFragment();
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
        return inflater.inflate(R.layout.fragment_subcategory_managment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        subcategoryListView = view.findViewById(R.id.subCategoriesListView);
        getSubcategories();
        subcategoryListView.setOnItemClickListener((parent, view1, position, id) -> FragmentTransition.to(SubcategoryEditFragment.newInstance(false, subcategoryListAdapter.getItem(position)),
                requireActivity(), true, R.id.categoryManagementContainer, "subcategoryManagement"));

        view.findViewById(R.id.addNewSubcategory).setOnClickListener(v -> FragmentTransition.to(SubcategoryEditFragment.newInstance(true, new Subcategory()),
                requireActivity(), true, R.id.categoryManagementContainer, "subcategoryManagement"));
    }

    @Override
    public void onResume() {
        super.onResume();
        getSubcategories();
    }

    private void getSubcategories(){
        CloudStoreUtil.selectSubCategories(result -> {
            subcategories = result;
            subcategoryListAdapter = new SubcategoryListAdapter(getActivity(), subcategories);
            subcategoryListView.setAdapter(subcategoryListAdapter);
        });
    }
}