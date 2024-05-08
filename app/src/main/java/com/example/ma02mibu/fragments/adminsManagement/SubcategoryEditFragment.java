package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.adminsManagment.CategoryListAdapter;
import com.example.ma02mibu.databinding.FragmentSubcategoryEditBinding;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.Subcategory;

public class SubcategoryEditFragment extends Fragment {

    private EditText name;
    private EditText description;
    private Spinner type;
    private ListView categoryListView;
    private Category selectedCategory = null;
    private EditText categoryName;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean isSubcategoryNew;
    private Subcategory subcategory;

    public SubcategoryEditFragment() { }

    public static SubcategoryEditFragment newInstance(boolean param1, Subcategory param2) {
        SubcategoryEditFragment fragment = new SubcategoryEditFragment();
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
            isSubcategoryNew = getArguments().getBoolean(ARG_PARAM1);
            subcategory = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSubcategoryEditBinding binding = FragmentSubcategoryEditBinding.inflate(inflater, container, false);

        name = binding.subcategoryName;
        description = binding.subcategoryDescription;
        type = binding.subcategoryType;
        categoryName = binding.categoryName;
        categoryName.setEnabled(false);
        categoryListView = binding.categoriesListView;


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getCategories();
        if(!isSubcategoryNew){
            getCurrentCategory();
        }

        type.setAdapter(new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.subcategory_types)));
        type.setEnabled(isSubcategoryNew);
        if (!isSubcategoryNew) {
            if (subcategory.getType() == Subcategory.SUBCATEGORYTYPE.USLUGA) {
                type.setSelection(0);
            }
            else {
                type.setSelection(1);
            }
        }

        view.findViewById(R.id.saveSubcategoryChanges).setOnClickListener(v -> {
            if(!fieldsAreValid()){
                return;
            }
            if(isSubcategoryNew){
                Subcategory newSubcategory = new Subcategory(selectedCategory.getDocumentRefId(), name.getText().toString(), description.getText().toString(), Subcategory.SUBCATEGORYTYPE.valueOf((String) type.getSelectedItem()));
                CloudStoreUtil.insertSubcategory(newSubcategory);
            }else {
                subcategory.setName(name.getText().toString());
                subcategory.setDescription(description.getText().toString());
                CloudStoreUtil.updateSubCategory(subcategory);
            }
            FragmentTransition.goBack(requireActivity(), "subcategoryManagement");
        });

        Button deleteButton = view.findViewById(R.id.deleteSubcategory);
        deleteButton.setOnClickListener(v -> {
            CloudStoreUtil.deleteSubCategory(subcategory);
            FragmentTransition.goBack(requireActivity(), "subcategoryManagement");
        });
        deleteButton.setEnabled(!isSubcategoryNew);

        if (!isSubcategoryNew){
            name.setText(subcategory.getName());
            description.setText(subcategory.getDescription());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCategories();
    }

    private void getCurrentCategory(){
        CloudStoreUtil.selectCategoryById(subcategory.getCategoryId(), result -> {
            selectedCategory = result;
            categoryName.setText(selectedCategory.getName());
        });
    }

    private void getCategories(){
        CloudStoreUtil.selectCategories(result -> {
            categoryListView.setAdapter(new CategoryListAdapter(getActivity(), result));
            categoryListView.setOnItemClickListener((parent, view, position, id) -> {
                selectedCategory = (Category) categoryListView.getAdapter().getItem(position);
                categoryName.setText(selectedCategory.getName());
            });
        });
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
        if(selectedCategory == null){
            categoryName.setError("Select category from the list");
            valid = false;
        }
        else{
            categoryName.setError(null);
        }

        return valid;
    }

}