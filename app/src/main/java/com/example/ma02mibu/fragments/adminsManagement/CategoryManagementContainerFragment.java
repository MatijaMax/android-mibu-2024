package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryManagementContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryManagementContainerFragment extends Fragment {
    public CategoryManagementContainerFragment() { }

    public static CategoryManagementContainerFragment newInstance(String param1, String param2) {
        CategoryManagementContainerFragment fragment = new CategoryManagementContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransition.to(CategoriesManagementFragment.newInstance(), getActivity(), true, R.id.categoryManagementContainer, "management");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_management_container, container, false);
    }
}