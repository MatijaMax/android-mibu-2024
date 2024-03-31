package com.example.ma02mibu.fragments.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.NewProductBinding;
import com.example.ma02mibu.databinding.ServicesPageFragmentBinding;
import com.example.ma02mibu.fragments.HomeFragment;
import com.example.ma02mibu.fragments.services.ServicesListFragment;

public class NewProduct extends Fragment {
    NewProductBinding binding;
    public static NewProduct newInstance() {
        return new NewProduct();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = NewProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}