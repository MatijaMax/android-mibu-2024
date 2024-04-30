package com.example.ma02mibu.fragments.products;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.ma02mibu.R;
import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.databinding.ProductsPageFragmentBinding;
import com.example.ma02mibu.fragments.services.ServicesListFragment;
import com.example.ma02mibu.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ProductsPageFragment extends Fragment {

    public static ArrayList<Product> products = new ArrayList<Product>();
    private ProductsPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProductsPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prepareProductList(products);

        FragmentTransition.to(ProductsListFragment.newInstance(), getActivity(),
                true, R.id.scroll_products_list, "productPage");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareProductList(ArrayList<Product> products){
        products.clear();
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.product1);
        images.add(R.drawable.right_button);
        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add("Svadbe");
        eventTypes.add("rodjendani");
        products.add(new Product(1L, "Product 1", "Opis 1", "kategorija 1", "podkategorija 1", 2000, images, eventTypes, 5));
        products.add(new Product(1L, "Proizvod 2", "Opis 2", "kategorija 2", "podkategorija 2", 2000, images, eventTypes, 0));
    }
}
