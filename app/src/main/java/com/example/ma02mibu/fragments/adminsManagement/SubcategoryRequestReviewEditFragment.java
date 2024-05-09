package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.adminsManagment.CategoryListAdapter;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.Subcategory;
import com.example.ma02mibu.model.SubcategoryProposal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SubcategoryRequestReviewEditFragment extends Fragment {

    private EditText name;
    private EditText description;
    private Spinner type;
    private ListView categoryListView;
    private Category selectedCategory = null;
    private EditText categoryName;


    private static final String ARG_PARAM1 = "param1";
    private SubcategoryProposal subcategoryProposal;

    public SubcategoryRequestReviewEditFragment() { }

    public static SubcategoryRequestReviewEditFragment newInstance(SubcategoryProposal param1) {
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
            subcategoryProposal = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory_request_review_edit, container, false);

        name = view.findViewById(R.id.subcategoryRequestName);
        description = view.findViewById(R.id.subcategoryRequestDescription);
        type = view.findViewById(R.id.subcategoryRequestType);

        categoryName = view.findViewById(R.id.categoryName);
        categoryName.setEnabled(false);
        categoryListView = view.findViewById(R.id.categoriesListView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getCategories();
        type.setAdapter(new ArrayAdapter<String>(requireActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.subcategory_types)));
        if (subcategoryProposal.getSubcategory().getType() == Subcategory.SUBCATEGORYTYPE.USLUGA) {
            type.setSelection(0);
        }
        else {
            type.setSelection(1);
        }

        name.setText(subcategoryProposal.getSubcategory().getName());
        description.setText(subcategoryProposal.getSubcategory().getDescription());

        view.findViewById(R.id.acceptSubcategoryRequest).setOnClickListener(v -> {
            if(!fieldsAreValid()){
                return;
            }
            CloudStoreUtil.insertSubcategory(new Subcategory(selectedCategory.getDocumentRefId(),
                                                            name.getText().toString(),
                                                            description.getText().toString(),
                                                            Subcategory.SUBCATEGORYTYPE.values()[type.getSelectedItemPosition()]));
            CloudStoreUtil.deleteSubcategoryProposal(subcategoryProposal);
            if(subcategoryProposal.getSubcategory().getType() == Subcategory.SUBCATEGORYTYPE.PROIZVOD)
                CloudStoreUtil.acceptProduct(subcategoryProposal.getItemId());
            else
                CloudStoreUtil.acceptService(subcategoryProposal.getItemId());
            OurNotification not = new OurNotification(subcategoryProposal.getOwnerMail(), "Sub category accepted", subcategoryProposal.getSubcategory().getName()+ " subcategory accepted", "notRead");
            CloudStoreUtil.insertNotification(not);
        });

        view.findViewById(R.id.rejectSubcategoryRequest).setOnClickListener(v -> {
            CloudStoreUtil.deleteSubcategoryProposal(subcategoryProposal);
        });

        view.findViewById(R.id.acceptSuggestedSubcategory).setOnClickListener(v -> {
            //TODO this is the stupid one
        });


        //TODO change
        Spinner subcategorySuggestionSpinner = view.findViewById(R.id.suggestedSubcategories);
        subcategorySuggestionSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<String>(Arrays.asList(new String[]{"Idemo bre", "Nesto drugo"}))));
    }

    private void getCategories(){
        CloudStoreUtil.selectCategories(result -> {
            categoryListView.setAdapter(new CategoryListAdapter(getActivity(), result));
            categoryListView.setOnItemClickListener((parent, view, position, id) -> {
                selectedCategory = (Category) categoryListView.getAdapter().getItem(position);
                categoryName.setText(selectedCategory.getName());
            });
            for(Category c: result){
                if(Objects.equals(c.getDocumentRefId(), subcategoryProposal.getSubcategory().getCategoryId())){
                    selectedCategory = c;
                    categoryName.setText(selectedCategory.getName());
                    break;
                }
            }
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