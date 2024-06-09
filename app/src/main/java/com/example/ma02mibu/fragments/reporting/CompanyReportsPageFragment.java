package com.example.ma02mibu.fragments.reporting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.CompanyReportsPageFragmentBinding;
import com.example.ma02mibu.databinding.ServicesPageFragmentBinding;
import com.example.ma02mibu.fragments.services.ServicesListFragment;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class CompanyReportsPageFragment extends Fragment {
    private CompanyReportsPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CompanyReportsPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FragmentTransition.to(CompanyReportsListFragment.newInstance(), getActivity(),
                true, R.id.scroll_company_reports_list, "companyReports");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
