package com.example.ma02mibu.fragments.pricelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.adapters.ProductPricelistAdapter;
import com.example.ma02mibu.databinding.PricelistPageFragmentBinding;
import com.example.ma02mibu.databinding.ProductsPricelistFragmentBinding;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ProductsPricelistFragment extends ListFragment {
    private ProductsPricelistFragmentBinding binding;
    private ArrayList<Product> mProducts;
    private boolean isOwner = false;
    private String userId;
    private FirebaseAuth auth;
    private ProductPricelistAdapter adapter;

    public static ProductsPricelistFragment newInstance(){
        ProductsPricelistFragment fragment = new ProductsPricelistFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }
        super.onCreate(savedInstanceState);
        CloudStoreUtil.selectProducts(new CloudStoreUtil.ProductCallback() {
            @Override
            public void onCallback(ArrayList<Product> retrievedProducts) {
                if (retrievedProducts != null) {
                    mProducts = retrievedProducts;
                } else {
                    mProducts = new ArrayList<>();
                }
                CloudStoreUtil.getOwner(userId, new CloudStoreUtil.OwnerCallback() {
                    @Override
                    public void onSuccess(Owner myItem) {
                        isOwner = true;
                        mProducts.removeIf(p -> !p.getOwnerUuid().equals(userId));
                        mProducts.removeIf(p -> p.isPending());
                        adapter = new ProductPricelistAdapter(getActivity(), mProducts);
                        setListAdapter(adapter);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        isOwner = false;
                    }
                });
            }
        });
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProductsPricelistFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}