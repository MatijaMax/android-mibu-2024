package com.example.ma02mibu.fragments.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.ProductsPageFragmentBinding;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.databinding.FragmentExploreAndFilterBinding;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreAndFilter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreAndFilter extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static ArrayList<Product> products = new ArrayList<Product>();
    private FragmentExploreAndFilterBinding binding;

    // TODO: Rename and change types and number of parameters
    public static ExploreAndFilter newInstance(String param1, String param2) {
        ExploreAndFilter fragment = new ExploreAndFilter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*@Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_and_filter, container, false);
    } */

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreAndFilterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prepareProductList(products);
        FragmentTransition.to(ProductsFilterFragment.newInstance(products), getActivity(),
                true, R.id.scroll_products_list, "filterAllPage");

        return root;
    }

    private void prepareProductList(ArrayList<Product> products){
        products.add(new Product(1L, "Proizvod 1", "Opis 1", "kategorija 1", "podkategorija 1", "1900", R.drawable.product1));
        products.add(new Product(1L, "Proizvod 2", "Opis 2", "kategorija 2", "podkategorija 2", "1900", R.drawable.product1));
    }

}
