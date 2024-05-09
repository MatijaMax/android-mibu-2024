package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.FragmentCategoryEditBinding;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.Owner;

public class CategoryEditFragment extends Fragment {

    private EditText name;
    private EditText description;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean isCategoryNew;
    private Category category;

    public CategoryEditFragment() { }

    public static CategoryEditFragment newInstance(boolean param1, Category param2) {
        CategoryEditFragment fragment = new CategoryEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isCategoryNew = getArguments().getBoolean(ARG_PARAM1);
            category = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCategoryEditBinding binding = FragmentCategoryEditBinding.inflate(inflater, container, false);

        name = binding.categoryName;
        description = binding.categoryDescription;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        name.setText(category.getName());
        description.setText(category.getDescription());
        view.findViewById(R.id.saveCategoryChanges).setOnClickListener(v -> {
            if(!fieldsAreValid()){
                return;
            }
            if(isCategoryNew){
                CloudStoreUtil.insertCategory(new Category(name.getText().toString(), description.getText().toString()));
            }else {
                if(!category.getName().equals(name.getText().toString())) {
                    CloudStoreUtil.selectOwners(result -> {
                        for(Owner o: result){
                            CloudStoreUtil.insertNotification(new OurNotification(o.getEmail(), "Category name changed", "Name changed from: " + category.getName() + " to: " + name.getText().toString(),"notRead"));
                        }
                    });
                    category.setName(name.getText().toString());
                }
                category.setDescription(description.getText().toString());
                CloudStoreUtil.updateCategory(category);
            }
            FragmentTransition.goBack(requireActivity(), "categoryManagement");
        });

        Button deleteCategory = view.findViewById(R.id.deleteCategory);
        deleteCategory.setOnClickListener(v -> {
            CloudStoreUtil.deleteCategory(category);
            FragmentTransition.goBack(requireActivity(), "categoryManagement");
        });
        deleteCategory.setEnabled(!isCategoryNew);
    }

    private boolean fieldsAreValid() {
        boolean valid = true;
        if(name.getText().toString().isEmpty()){
            name.setError("Name is required");
            valid = false;
        }
        else {
            name.setError(null);
        }
        if(description.getText().toString().isEmpty()){
            description.setError("Description is required");
            valid = false;
        }
        else{
            description.setError(null);
        }
        return valid;
    }

}