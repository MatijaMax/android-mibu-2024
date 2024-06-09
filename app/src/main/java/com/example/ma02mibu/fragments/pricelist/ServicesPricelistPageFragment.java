package com.example.ma02mibu.fragments.pricelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.ProductsPricelistPageFragmentBinding;
import com.example.ma02mibu.databinding.ServicesPricelistPageFragmentBinding;

public class ServicesPricelistPageFragment extends Fragment {

    private ServicesPricelistPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ServicesPricelistPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FragmentTransition.to(ServicesPricelistFragment.newInstance(), getActivity(),
                true, R.id.scroll_services_pricelist, "servicesPricelist1");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
