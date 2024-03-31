package com.example.ma02mibu.fragments.adminsManagment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.adminsManagment.CategoryPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesManagmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesManagmentFragment extends Fragment {
    private CategoryPagerAdapter categoryPagerAdapter;
    private ViewPager2 viewPager;
    public CategoriesManagmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoriesManagmentFragment.
     */
    public static CategoriesManagmentFragment newInstance() {
        CategoriesManagmentFragment fragment = new CategoriesManagmentFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories_managment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        categoryPagerAdapter = new CategoryPagerAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(categoryPagerAdapter);

        final String[] tabTitles = {"Manage categories", "Manage subcategories", "Manage subcategory requests"};
        //final int[] tabIcons = {R.drawable.something, R.drawable.something, R.drawable.something};

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(tabTitles[position]);
                    //tab.setIcon(tabIcons[position]);
                }
        ).attach();
    }
}