package com.example.ma02mibu.fragments.employees;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.EmployeeListAdapter;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.databinding.FragmentEmployeeCardBinding;
import com.example.ma02mibu.databinding.FragmentEmployeeListBinding;
import com.example.ma02mibu.fragments.products.NewProduct;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class EmployeeListFragment extends ListFragment {
    private FragmentEmployeeListBinding binding;
    private ArrayList<Employee> mEmployees;
    private EmployeeListAdapter adapter;
    private static final String ARG_EMPLOYEES = "employees";

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

        CloudStoreUtil.selectEmployees("LpDHEOT9JWhVNrHWP20K", new CloudStoreUtil.EmployeeCallback(){
            @Override
            public void onCallback(ArrayList<Employee> retrievedProducts) {
                if (retrievedProducts != null) {
                    mEmployees = retrievedProducts;
                    Log.i("CCCCCCCCCCCC","OKKKKKKKKKKKKKKKKKKKKK");
                    Log.i("RRRRRRRRRRRRRRRRRRRR", ""+mEmployees.toArray().length);
                } else {
                    mEmployees = new ArrayList<>();
                }

                adapter = new EmployeeListAdapter(getActivity(), mEmployees, getActivity());
                setListAdapter(adapter);
            }
        });

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mEmployees = getArguments().getParcelableArrayList(ARG_EMPLOYEES);
//            adapter = new EmployeeListAdapter(getActivity(), mEmployees, getActivity());
//            setListAdapter(adapter);
//        }


    }

    private static void load(){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        //mEmployees.clear();
    }

    public static EmployeeListFragment newInstance(){
        EmployeeListFragment fragment = new EmployeeListFragment();
//        Bundle args = new Bundle();
//        args.putParcelableArrayList(ARG_EMPLOYEES, employees);
//        fragment.setArguments(args);
        return fragment;
    }
}