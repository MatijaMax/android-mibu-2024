package com.example.ma02mibu.fragments.reporting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.OdReportsPageFragmentBinding;

public class ODReportsPageFragment extends Fragment {
    private OdReportsPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OdReportsPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FragmentTransition.to(ODReportsListFragment.newInstance(), getActivity(),
                true, R.id.scroll_od_reports_list, "od_reports");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
