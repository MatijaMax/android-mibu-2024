package com.example.ma02mibu.fragments.reporting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.CompanyReportsAdapter;
import com.example.ma02mibu.adapters.ServiceListAdapter;
import com.example.ma02mibu.databinding.CompanyReportsListBinding;
import com.example.ma02mibu.databinding.FragmentServicesListBinding;
import com.example.ma02mibu.fragments.services.NewService;
import com.example.ma02mibu.fragments.services.ServicesListFragment;
import com.example.ma02mibu.model.CompanyReport;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Service;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CompanyReportsListFragment extends ListFragment {
    private CompanyReportsListBinding binding;

    private CompanyReportsAdapter adapter;
    private ArrayList<CompanyReport> companyReports;
    private static final String ARG_PARAM = "param";
    public static CompanyReportsListFragment newInstance(){
        CompanyReportsListFragment fragment = new CompanyReportsListFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            CloudStoreUtil.selectCompanyReports(new CloudStoreUtil.CompanyReportCallback() {
                @Override
                public void onCallbackCompanyReports(ArrayList<CompanyReport> reports) {
                    if (reports != null) {
                        companyReports = reports;
                        adapter = new CompanyReportsAdapter(getActivity(), companyReports, getActivity());
                        setListAdapter(adapter);
                    } else {
                        companyReports = new ArrayList<>();
                    }

                }
            });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = CompanyReportsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
