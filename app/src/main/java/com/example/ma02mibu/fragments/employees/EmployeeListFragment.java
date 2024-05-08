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
import com.example.ma02mibu.databinding.FragmentEmployeeListBinding;
import com.example.ma02mibu.model.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class EmployeeListFragment extends ListFragment {
    private FirebaseAuth auth;
    private FragmentEmployeeListBinding binding;
    private ArrayList<Employee> mEmployees;
    private ArrayList<Employee> mEmployeesBackup;
    private EmployeeListAdapter adapter;

    private String ownerRefId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            ownerRefId = user.getUid();
        }

        binding = FragmentEmployeeListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Button newEmployeeButton = binding.btnAddNewEmployee;
        newEmployeeButton.setOnClickListener(v -> {
            FragmentTransition.to(EmployeeRegistrationFragment.newInstance(ownerRefId), getActivity(),
                    true, R.id.scroll_employees_list, "newEmployeePage");
        });


        Button searchBtn = binding.btnFilters;
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterEmployees();
            }
        });
        Log.i("MMMMMMMMMMMMMM", ownerRefId);
        loadEmployees();

        return root;
    }

    private void loadEmployees() {
        CloudStoreUtil.getEmployeesList(ownerRefId, new CloudStoreUtil.EmployeesListCallback() {
            @Override
            public void onSuccess(ArrayList<Employee> itemList) {
                // Handle the retrieved list of items (e.g., display them in UI)
                mEmployees = new ArrayList<>(itemList);
                mEmployeesBackup = new ArrayList<>(itemList);
                adapter = new EmployeeListAdapter(getActivity(), mEmployees, ownerRefId, getActivity());
                setListAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error fetching documents: " + e.getMessage());
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void filterEmployees(){
        String fName = binding.searchFirstName.getQuery().toString();
        String lName = binding.searchLastName.getQuery().toString();
        String email = binding.searchLastName.getQuery().toString();
        mEmployees=new ArrayList<>(mEmployeesBackup);
        Log.i("RRRRRRRRR",""+mEmployeesBackup.toArray().length);
        if(fName.isEmpty() && lName.isEmpty() && email.isEmpty()){
            adapter = new EmployeeListAdapter(getActivity(), mEmployees, ownerRefId, getActivity());
            setListAdapter(adapter);
            return;
        }
        if(!fName.isEmpty())
            mEmployees.removeIf(employee -> !employee.getFirstName().toLowerCase().contains(fName.toLowerCase()));
        if(!lName.isEmpty())
            mEmployees.removeIf(employee -> !employee.getLastName().toLowerCase().contains(lName.toLowerCase()));
        if(!email.isEmpty())
            mEmployees.removeIf(employee -> !employee.getEmail().toLowerCase().contains(email.toLowerCase()));
        adapter = new EmployeeListAdapter(getActivity(), mEmployees, ownerRefId, getActivity());
        setListAdapter(adapter);
    }

    private EmployeeListFragment(){}

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