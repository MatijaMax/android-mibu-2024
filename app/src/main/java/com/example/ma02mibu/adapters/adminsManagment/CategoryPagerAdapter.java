package com.example.ma02mibu.adapters.adminsManagment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ma02mibu.fragments.adminsManagement.CategoryManagementTabFragment;
import com.example.ma02mibu.fragments.adminsManagement.SubcategoryManagmentTabFragment;
import com.example.ma02mibu.fragments.adminsManagement.SubcategoryRequestReviewTabFragment;

public class CategoryPagerAdapter extends FragmentStateAdapter {
    public CategoryPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                Fragment categoryTab = new CategoryManagementTabFragment();
                return categoryTab;
            case 1:
                Fragment subcategoryTab = new SubcategoryManagmentTabFragment();
                return subcategoryTab;
            case 2:
                Fragment subcategoryReviewTab = new SubcategoryRequestReviewTabFragment();
                return subcategoryReviewTab;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
