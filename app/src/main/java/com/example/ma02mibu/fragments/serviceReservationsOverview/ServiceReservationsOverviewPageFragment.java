package com.example.ma02mibu.fragments.serviceReservationsOverview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentEmployeePageBinding;
import com.example.ma02mibu.databinding.FragmentServiceReservationsOverviewPageBinding;
import com.example.ma02mibu.fragments.employees.EmployeeListFragment;

public class ServiceReservationsOverviewPageFragment extends Fragment {

    private FragmentServiceReservationsOverviewPageBinding binding;

    public ServiceReservationsOverviewPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceReservationsOverviewPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(ServiceReservationsOrganizerOverviewFragment.newInstance("",""), getActivity(),
                true, R.id.scroll_services_res_list, "servicesResList");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}