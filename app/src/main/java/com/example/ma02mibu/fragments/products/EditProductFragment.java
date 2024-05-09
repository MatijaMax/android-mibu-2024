package com.example.ma02mibu.fragments.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.databinding.EditProductBinding;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;
import java.util.UUID;

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
        productPrice.setText(String.valueOf(mProduct.getPrice()));
        EditText productDiscount = binding.ProductDiscountEdit;
        productDiscount.setText(String.valueOf(mProduct.getDiscount()));
        CheckBox visibleCheckBox = binding.checkBoxODAvailableEdit;
        visibleCheckBox.setChecked(mProduct.isVisible());
        CheckBox buyAvailableCheckBox = binding.checkBoxBuyAvailableEdit;
        buyAvailableCheckBox.setChecked(mProduct.isAvailableToBuy());
        Button submitBtn = binding.submitButton;
        submitBtn.setOnClickListener(v -> editProduct());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProduct = getArguments().getParcelable(ARG_PARAM);
        }
    }


    private void editProduct(){
        String name = binding.ProductNameEdit.getText().toString();
        String description = binding.ProductDescriptionEdit.getText().toString();
        String price = binding.ProductPriceEdit.getText().toString();
        String discount = binding.ProductDiscountEdit.getText().toString();
        boolean visible = binding.checkBoxODAvailableEdit.isChecked();
        boolean isAvailableToBuy = binding.checkBoxBuyAvailableEdit.isChecked();
        int priceInt = Integer.parseInt(price);
        int discountInt = Integer.parseInt(discount);
        mProduct.setName(name);
        mProduct.setDescription(description);
        mProduct.setPrice(priceInt);
        mProduct.setDiscount(discountInt);
        mProduct.setVisible(visible);
        mProduct.setAvailableToBuy(isAvailableToBuy);
        CloudStoreUtil.updateProduct(mProduct);
        FragmentTransition.to(ProductsListFragment.newInstance(), getActivity(),
                false, R.id.scroll_products_list, "falsh");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
