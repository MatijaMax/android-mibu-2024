package com.example.ma02mibu.fragments.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.ServicesPageFragmentBinding;

public class ServicesPageFragment extends Fragment {

    private ServicesPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ServicesPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(ServicesListFragment.newInstance(), getActivity(),
                false, R.id.scroll_services_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
