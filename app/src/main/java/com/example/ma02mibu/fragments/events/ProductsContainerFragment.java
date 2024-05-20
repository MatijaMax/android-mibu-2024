package com.example.ma02mibu.fragments.events;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
public class ProductsContainerFragment extends Fragment {
    public ProductsContainerFragment() { }

    public static ProductsContainerFragment newInstance() {
        ProductsContainerFragment fragment = new ProductsContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTransition.to(SelectEventFragment.newInstance(), getActivity(), true, R.id.products_container, "productsManagement");
        return inflater.inflate(R.layout.fragment_products_container, container, false);
    }
}