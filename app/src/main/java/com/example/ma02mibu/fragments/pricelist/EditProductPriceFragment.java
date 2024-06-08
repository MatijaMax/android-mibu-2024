package com.example.ma02mibu.fragments.pricelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.PackagesPricelistAdapter;
import com.example.ma02mibu.databinding.EditProductPriceFragmentBinding;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class EditProductPriceFragment extends Fragment {
    private EditProductPriceFragmentBinding binding;
    private Product mProduct;
    private static final String ARG_PARAM = "param";
    public static EditProductPriceFragment newInstance(Product product) {
        EditProductPriceFragment fragment = new EditProductPriceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, product);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditProductPriceFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView productName = binding.ProductName;
        productName.setText(mProduct.getName());
        EditText productPrice = binding.ProductPriceEdit;
        productPrice.setText(String.valueOf(mProduct.getPrice()));
        EditText productDiscount = binding.ProductDiscountEdit;
        productDiscount.setText(String.valueOf(mProduct.getDiscount()));
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
        String price = binding.ProductPriceEdit.getText().toString();
        String discount = binding.ProductDiscountEdit.getText().toString();
        int priceInt = Integer.parseInt(price);
        int discountInt = Integer.parseInt(discount);
        mProduct.setPrice(priceInt);
        mProduct.setDiscount(discountInt);
        CloudStoreUtil.updateProduct(mProduct);
        updatePriceInPackages(mProduct);
        FragmentTransition.to(ProductsPricelistFragment.newInstance(), getActivity(),
                false, R.id.scroll_product_pricelist, "falsh");
    }
    private void updatePriceInPackages(Product product){
        CloudStoreUtil.selectPackages(new CloudStoreUtil.PackageCallback(){
            @Override
            public void onCallbackPackage(ArrayList<Package> retrievedPackages) {
                boolean save;
                for(Package p: retrievedPackages){
                    save = false;
                    for(Product prod: p.getProducts()){
                        if(prod.getFirestoreId().equals(product.getFirestoreId())){
                            prod.setPrice(product.getPrice());
                            save = true;
                        }
                    }
                    if(save)
                        CloudStoreUtil.updatePackage(p);
                }
            }
        });
    }
}
