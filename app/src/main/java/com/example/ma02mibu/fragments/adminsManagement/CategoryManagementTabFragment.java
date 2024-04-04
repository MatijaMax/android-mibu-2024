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
import com.example.ma02mibu.adapters.adminsManagment.CategoryListAdapter;
import com.example.ma02mibu.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CategoryManagementTabFragment extends Fragment {
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
        createCategories();
        categoryListAdapter = new CategoryListAdapter(getActivity(), categories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_management_tab, container, false);

        ListView categoryListView = view.findViewById(R.id.categoriesListView);
        categoryListView.setAdapter(categoryListAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransition.to(CategoryEditFragment.newInstance(false, categoryListAdapter.getItem(position)),
                        getActivity(), true, R.id.categoryManagementContainer, "categoryManagement");
            }
        });

        ((FloatingActionButton) view.findViewById(R.id.addNewCategory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(CategoryEditFragment.newInstance(true, new Category()),
                        getActivity(), true, R.id.categoryManagementContainer, "categoryManagement");
            }
        });

        return view;
    }
    private void createCategories(){
        categories.add(new Category(1L, "Ime kategorije 1", "Opis kategorije 1"));
        categories.add(new Category(2L, "Ime kategorije 2", "Opis kategorije 2"));
        categories.add(new Category(3L, "Ime kategorije 3", "Opis kategorije 3"));
        categories.add(new Category(4L, "Ime kategorije 4", "Opis kategorije 4"));
        categories.add(new Category(5L, "Ime kategorije 5", "Opis kategorije 5"));
        categories.add(new Category(6L, "Ime kategorije 6", "Opis kategorije 6"));
        categories.add(new Category(7L, "Ime kategorije 7", "Opis kategorije 7"));
    }
}