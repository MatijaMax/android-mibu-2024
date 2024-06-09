package com.example.ma02mibu.fragments.reporting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.CompanyReportsAdapter;
import com.example.ma02mibu.adapters.ODReportsAdapter;
import com.example.ma02mibu.databinding.CompanyReportsListBinding;
import com.example.ma02mibu.databinding.OdReportsListBinding;
import com.example.ma02mibu.model.CompanyReport;
import com.example.ma02mibu.model.ODReport;

import java.util.ArrayList;

public class ODReportsListFragment extends ListFragment {
    private OdReportsListBinding binding;

    private ODReportsAdapter adapter;
    private ArrayList<ODReport> odReports;
    public static ODReportsListFragment newInstance(){
        ODReportsListFragment fragment = new ODReportsListFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CloudStoreUtil.selectODReports(new CloudStoreUtil.ODReportCallback() {
            @Override
            public void onCallbackODReports(ArrayList<ODReport> reports) {
                if (reports != null) {
                    odReports = reports;
                    adapter = new ODReportsAdapter(getActivity(), odReports, getActivity());
                    setListAdapter(adapter);
                } else {
                    odReports = new ArrayList<>();
                }

            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = OdReportsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
