package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.adminsManagment.SubcategoryListAdapter;
import com.example.ma02mibu.model.SubCategory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SubcategoryManagmentTabFragment extends Fragment {

    private SubcategoryListAdapter subCategoryListAdapter;
    private ArrayList<SubCategory> subCategories = new ArrayList<>();
    public SubcategoryManagmentTabFragment() { }

    public static SubcategoryManagmentTabFragment newInstance(String param1, String param2) {
        SubcategoryManagmentTabFragment fragment = new SubcategoryManagmentTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSubCategories();
        subCategoryListAdapter = new SubcategoryListAdapter(getActivity(), subCategories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subcategory_managment_tab, container, false);

        ListView subCategoryListView = view.findViewById(R.id.subCategoriesListView);
        subCategoryListView.setAdapter(subCategoryListAdapter);
        subCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransition.to(SubcategoryEditFragment.newInstance(false, subCategoryListAdapter.getItem(position)),
                        getActivity(), true, R.id.categoryManagementContainer, "subcategoryManagement");
            }
        });

        ((FloatingActionButton) view.findViewById(R.id.addNewSubcategory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(SubcategoryEditFragment.newInstance(true, new SubCategory()),
                        getActivity(), true, R.id.categoryManagementContainer, "subcategoryManagement");
            }
        });

        return view;
    }
    private void createSubCategories(){
        subCategories.add(new SubCategory(1L, 1L, "Ime podkategorije 1", "Opis podkategorije 1", SubCategory.SUBCATEGORYTYPE.PROIZVOD));
        subCategories.add(new SubCategory(2L, 1L, "Ime podkategorije 2", "Opis podkategorije 2", SubCategory.SUBCATEGORYTYPE.USLUGA));
        subCategories.add(new SubCategory(3L, 3L, "Ime podkategorije 3", "Opis podkategorije 3", SubCategory.SUBCATEGORYTYPE.PROIZVOD));
        subCategories.add(new SubCategory(4L, 3L, "Ime podkategorije 4", "Opis podkategorije 4", SubCategory.SUBCATEGORYTYPE.PROIZVOD));
        subCategories.add(new SubCategory(5L, 3L, "Ime podkategorije 5", "Opis podkategorije 5", SubCategory.SUBCATEGORYTYPE.USLUGA));
        subCategories.add(new SubCategory(6L, 2L, "Ime podkategorije 6", "Opis podkategorije 6", SubCategory.SUBCATEGORYTYPE.USLUGA));
        subCategories.add(new SubCategory(7L, 2L, "Ime podkategorije 7", "Opis podkategorije 7", SubCategory.SUBCATEGORYTYPE.USLUGA));
        subCategories.add(new SubCategory(8L, 1L, "Ime podkategorije 8", "Opis podkategorije 8", SubCategory.SUBCATEGORYTYPE.PROIZVOD));
    }
}