package com.example.ma02mibu.fragments.packages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.databinding.ChooseProductsListBinding;
import com.example.ma02mibu.databinding.FragmentProductsListBinding;
import com.example.ma02mibu.fragments.products.NewProduct;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.viewmodels.PackageEditViewModel;
import com.example.ma02mibu.viewmodels.PackageViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ChooseProductsListFragment extends ListFragment {
    private ChooseProductsListBinding binding;
    private ArrayList<Product> mProducts;
    public ArrayList<Product> productsChosen = new ArrayList<>();
    private ProductListAdapter adapter;
    private PackageViewModel viewModel;
    private PackageEditViewModel editViewModel;
    private int productsChosenNum;
    private boolean isFromEdit;
    private static final String ARG_PARAM = "param";
    public static ChooseProductsListFragment newInstance(){
        ChooseProductsListFragment fragment = new ChooseProductsListFragment();
        return fragment;
    }

    public static ChooseProductsListFragment newInstance(ArrayList<Product> products){
        ChooseProductsListFragment fragment = new ChooseProductsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, products);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        productsChosenNum = 0;
        isFromEdit = false;
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productsChosen = getArguments().getParcelableArrayList(ARG_PARAM);
            productsChosenNum = productsChosen.size();
            isFromEdit = true;
        }
        ChooseProductsListFragment fragment = this;
        CloudStoreUtil.selectProducts(new CloudStoreUtil.ProductCallback(){
            @Override
            public void onCallback(ArrayList<Product> retrievedProducts) {
                if (retrievedProducts != null) {
                    mProducts = retrievedProducts;
                } else {
                    mProducts = new ArrayList<>();
                }
                adapter = new ProductListAdapter(getActivity(), mProducts, getActivity(), true, fragment);
                setListAdapter(adapter);
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = ChooseProductsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button button = binding.submitProductsButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFromEdit) {
                    viewModel = new ViewModelProvider(requireActivity()).get(PackageViewModel.class);
                    viewModel.setProducts(productsChosen);
                    FragmentTransition.to(NewPackage.newInstance(), getActivity(),
                            true, R.id.scroll_packages_list, "newPackagePage");
                }else{
                    viewModel = new ViewModelProvider(requireActivity()).get(PackageViewModel.class);
                    viewModel.setProducts(productsChosen);
                    FragmentTransition.to(EditPackageFragment.newInstance(), getActivity(),
                            false, R.id.scroll_packages_list, "falsh");
                }
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void productChosen(String id){
        productsChosenNum++;
        TextView textView = binding.productsChosenNum;
        String s = productsChosenNum+ " products chosen";
        textView.setText(s);
        Product product = mProducts.stream().filter(p -> p.getFirestoreId().equals(id)).findFirst().orElse(null);
        productsChosen.add(product);
    }
    public void productUnChosen(String id){
        productsChosenNum--;
        TextView textView = binding.productsChosenNum;
        String s = productsChosenNum+ " products chosen";
        textView.setText(s);
        productsChosen.removeIf(p -> p.getFirestoreId().equals(id));
    }
}
