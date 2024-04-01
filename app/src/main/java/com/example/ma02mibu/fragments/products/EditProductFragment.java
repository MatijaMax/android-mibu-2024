package com.example.ma02mibu.fragments.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.databinding.EditProductBinding;
import com.example.ma02mibu.model.Product;

public class EditProductFragment extends Fragment {
    private EditProductBinding binding;
    private Product mProduct;
    private static final String ARG_PARAM = "param";
    public static EditProductFragment newInstance(Product product) {
        EditProductFragment fragment = new EditProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, product);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText productName = binding.ProductNameEdit;
        productName.setText(mProduct.getName());
        EditText productDescription = binding.ProductDescriptionEdit;
        productDescription.setText(mProduct.getDescription());
        EditText productPrice = binding.ProductPriceEdit;
        productPrice.setText(mProduct.getPrice());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProduct = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
