package com.example.ma02mibu.fragments.employees;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.EmployeeListAdapter;
import com.example.ma02mibu.databinding.FragmentEmployeeCardBinding;
import com.example.ma02mibu.databinding.FragmentEmployeeListBinding;
import com.example.ma02mibu.fragments.products.NewProduct;
import com.example.ma02mibu.model.Employee;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class EmployeeListFragment extends ListFragment {
    private FragmentEmployeeListBinding binding;
    private ArrayList<Employee> mEmployees;
    private EmployeeListAdapter adapter;
    private static final String ARG_EMPLOYEES = "employees";

    public EmployeeListFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentEmployeeListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button newEmployeeButton = binding.btnAddNewEmployee;
        newEmployeeButton.setOnClickListener(v -> {
            FragmentTransition.to(EmployeeRegistrationFragment.newInstance("",""), getActivity(),
                    true, R.id.scroll_employees_list, "newEmployeePage");
        });

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEmployees = getArguments().getParcelableArrayList(ARG_EMPLOYEES);
            adapter = new EmployeeListAdapter(getActivity(), mEmployees);
            setListAdapter(adapter);
        }
    }

    public static EmployeeListFragment newInstance(ArrayList<Employee> employees){
        EmployeeListFragment fragment = new EmployeeListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_EMPLOYEES, employees);
        fragment.setArguments(args);
        return fragment;
    }
}