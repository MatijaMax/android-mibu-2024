package com.example.ma02mibu.fragments.events;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentBuyProductBinding;
import com.example.ma02mibu.model.Product;

public class BuyProductFragment extends Fragment {

    private static final String ARG_PARAM1 = "product";
    private Product product;

    public BuyProductFragment() { }

    public static BuyProductFragment newInstance(Product param1) {
        BuyProductFragment fragment = new BuyProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_buy_product, container, false);
    }
}