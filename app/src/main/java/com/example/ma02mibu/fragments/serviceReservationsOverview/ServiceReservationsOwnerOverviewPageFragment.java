package com.example.ma02mibu.fragments.serviceReservationsOverview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentServiceReservationsOverviewPageBinding;
import com.example.ma02mibu.databinding.FragmentServiceReservationsOwnerOverviewPageBinding;


public class ServiceReservationsOwnerOverviewPageFragment extends Fragment {


    private FragmentServiceReservationsOwnerOverviewPageBinding binding;

    public ServiceReservationsOwnerOverviewPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentServiceReservationsOwnerOverviewPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(ServiceReservationsOwnerOverviewFragment.newInstance("",""), getActivity(),
                true, R.id.scroll_services_res_list_owner, "servicesResOwnerList");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}