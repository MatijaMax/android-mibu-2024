package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.adminsManagment.CategoryListAdapter;
import com.example.ma02mibu.model.Category;

import java.util.ArrayList;

public class CategoryManagementTabFragment extends Fragment {
    private ListView categoryListView;
    private CategoryListAdapter categoryListAdapter;
    private ArrayList<Category> categories = new ArrayList<>();
    public CategoryManagementTabFragment() { }

    public static CategoryManagementTabFragment newInstance() {
        CategoryManagementTabFragment fragment = new CategoryManagementTabFragment();
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

        return inflater.inflate(R.layout.fragment_category_management_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        categoryListView = view.findViewById(R.id.categoriesListView);
        getCategories();

        categoryListView.setOnItemClickListener((parent, view1, position, id) -> FragmentTransition.to(CategoryEditFragment.newInstance(false, categoryListAdapter.getItem(position)),
                requireActivity(), true, R.id.categoryManagementContainer, "categoryManagement"));

        view.findViewById(R.id.addNewCategory).setOnClickListener(v -> FragmentTransition.to(CategoryEditFragment.newInstance(true, new Category()),
                requireActivity(), true, R.id.categoryManagementContainer, "categoryManagement"));
    }

    @Override
    public void onResume() {
        super.onResume();
        getCategories();
    }

    private void getCategories(){
        CloudStoreUtil.selectCategories(result -> {
            categories = result;
            categoryListAdapter = new CategoryListAdapter(getActivity(), categories);
            categoryListView.setAdapter(categoryListAdapter);
        });
    }
}