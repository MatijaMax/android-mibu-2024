package com.example.ma02mibu.fragments.pricelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.PackagesPageFragmentBinding;
import com.example.ma02mibu.databinding.ProductsPricelistFragmentBinding;
import com.example.ma02mibu.databinding.ProductsPricelistPageFragmentBinding;
import com.example.ma02mibu.fragments.packages.PackageListFragment;

public class ProductsPricelistPageFragment extends Fragment {

    private ProductsPricelistPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProductsPricelistPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FragmentTransition.to(ProductsPricelistFragment.newInstance(), getActivity(),
                true, R.id.scroll_product_pricelist, "productsPricelist1");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
