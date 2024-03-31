package com.example.ma02mibu.fragments.adminsManagment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.adminsManagment.CategoryListAdapter;
import com.example.ma02mibu.model.Category;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryManagmentTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryManagmentTabFragment extends Fragment {
    private CategoryListAdapter categoryListAdapter;
    private ArrayList<Category> categories = new ArrayList<>();
    public CategoryManagmentTabFragment() { }

    public static CategoryManagmentTabFragment newInstance() {
        CategoryManagmentTabFragment fragment = new CategoryManagmentTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_category_managment_tab, container, false);

        ListView categoriListView = view.findViewById(R.id.categoriesListView);
        categoriListView.setAdapter(categoryListAdapter);

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