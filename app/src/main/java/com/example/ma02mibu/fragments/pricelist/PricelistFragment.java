package com.example.ma02mibu.fragments.pricelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.PricelistPageFragmentBinding;

public class PricelistFragment extends Fragment {
    private PricelistPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PricelistPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button productsPricelist = binding.productsPricelist;
        productsPricelist.setOnClickListener(f -> {
            FragmentTransition.to(ProductsPricelistPageFragment.newInstance(), getActivity(),
                    true, R.id.scroll_product_pricelist, "productsPricelist");
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
