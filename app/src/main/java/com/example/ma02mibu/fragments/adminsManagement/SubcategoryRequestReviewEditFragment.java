package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.SubCategory;
import com.example.ma02mibu.model.SubCategoryRequest;

import java.util.ArrayList;
import java.util.Arrays;

public class SubcategoryRequestReviewEditFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private SubCategoryRequest subcategoryRequest;

    public SubcategoryRequestReviewEditFragment() { }

    public static SubcategoryRequestReviewEditFragment newInstance(SubCategoryRequest param1) {
        SubcategoryRequestReviewEditFragment fragment = new SubcategoryRequestReviewEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subcategoryRequest = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subcategory_request_review_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Spinner subcategoryRequestSpinner = view.findViewById(R.id.subcategoryRequestType);
        subcategoryRequestSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.subcategory_types)));
        if (subcategoryRequest.getType() == SubCategory.SUBCATEGORYTYPE.USLUGA) {
            subcategoryRequestSpinner.setSelection(0);
        }
        else {
            subcategoryRequestSpinner.setSelection(1);
        }

        Spinner subcategorySuggestionSpinner = view.findViewById(R.id.suggestedSubcategories);
        subcategorySuggestionSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<String>(Arrays.asList(new String[]{"Idemo bre", "Nesto drugo"}))));
    }

}