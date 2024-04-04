package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.SubCategory;

public class SubcategoryEditFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isSubcategoryNew;
    private SubCategory subCategory;

    public SubcategoryEditFragment() { }

    public static SubcategoryEditFragment newInstance(boolean param1, SubCategory param2) {
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
            subCategory = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subcategory_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Spinner typeSpinner = view.findViewById(R.id.subcategoryType);
        typeSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.subcategory_types)));
        typeSpinner.setEnabled(isSubcategoryNew);
        if (!isSubcategoryNew) {
            if (subCategory.getType() == SubCategory.SUBCATEGORYTYPE.USLUGA) {
                typeSpinner.setSelection(0);
            }
            else {
                typeSpinner.setSelection(1);
            }
        }

        ((Button) view.findViewById(R.id.saveSubcategoryChanges)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        Button deleteButton = view.findViewById(R.id.deleteSubcategory);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        deleteButton.setEnabled(!isSubcategoryNew);

        if (!isSubcategoryNew){
            ((EditText) view.findViewById(R.id.subcategoryName)).setText(subCategory.getName());
            ((EditText) view.findViewById(R.id.subcategoryDescription)).setText(subCategory.getDescription());
            ((EditText) view.findViewById(R.id.subcategoryCategory)).setText(subCategory.getCategoryId().toString());
        }
    }
}