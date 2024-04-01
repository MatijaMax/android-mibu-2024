package com.example.ma02mibu.fragments.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.ma02mibu.R;
import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.databinding.ProductsPageFragmentBinding;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class ProductsPageFragment extends Fragment {

    public static ArrayList<Product> products = new ArrayList<Product>();
    private ProductsPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProductsPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prepareProductList(products);

        FragmentTransition.to(ProductsListFragment.newInstance(products), getActivity(),
                true, R.id.scroll_products_list, "productPage");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareProductList(ArrayList<Product> products){
        products.add(new Product(1L, "Proizvod 1", "Opis 1", "kategorija 1", "podkategorija 1", "1900", R.drawable.product1));
        products.add(new Product(1L, "Proizvod 2", "Opis 2", "kategorija 2", "podkategorija 2", "1900", R.drawable.product1));
    }
}
