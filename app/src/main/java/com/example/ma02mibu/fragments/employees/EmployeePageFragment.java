package com.example.ma02mibu.fragments.employees;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentEmployeePageBinding;
import com.example.ma02mibu.fragments.employees.EmployeeListFragment;
import com.example.ma02mibu.model.Employee;

import java.util.ArrayList;

public class EmployeePageFragment extends Fragment {

    private FragmentEmployeePageBinding binding;
    public static ArrayList<Employee> employeesFake = new ArrayList<>();

    public EmployeePageFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmployeePageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Employee employee1 =
                new Employee(1L, "Ana", "Stevic", "ana@gmail.com", "123", "Veselina Maslese 12, Novi Sad", 63124, R.drawable.product1);
        Employee employee2 =
                new Employee(2L, "Marko", "Ilic", "m12@gmail.com", "123", "Kornelija Stankovica 18, Novi Sad", 54848, R.drawable.employee_avatar);
        Employee employee3 =
                new Employee(1L, "Ivan", "Milic", "ivo@gmail.com", "123", "Augustina Makarica 6, Novi Sad", 838732, R.drawable.employee_avatar);
        employeesFake.add(employee1);
        employeesFake.add(employee2);
        employeesFake.add(employee3);
        FragmentTransition.to(EmployeeListFragment.newInstance(employeesFake), getActivity(),
                true, R.id.scroll_employees_list, "employeesPage");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}