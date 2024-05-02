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
import com.example.ma02mibu.model.WorkSchedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
                new Employee(1L, "Ana", "Stevic", "ana@gmail.com", "123", "Veselina Maslese 12, Novi Sad", "063124", R.drawable.employee_avatar);
        Employee employee2 =
                new Employee(2L, "Marko", "Ilic", "m12@gmail.com", "123", "Kornelija Stankovica 18, Novi Sad", "054848", R.drawable.employee_avatar);
        Employee employee3 =
                new Employee(3L, "Ivan", "Milic", "ivo@gmail.com", "123", "Augustina Makarica 6, Novi Sad", "0838732", R.drawable.product1);
        Employee employee4 =
                new Employee(4L, "Petar", "Peric", "pera@gmail.com", "123", "Ilariona Ruvarca 16, Novi Sad", "03245544", R.drawable.employee_avatar);

        WorkSchedule companyWorkSchedule = new WorkSchedule();
        companyWorkSchedule.setWorkTime(DayOfWeek.MONDAY, LocalTime.NOON, LocalTime.of(15,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.TUESDAY, LocalTime.of(8,30), LocalTime.of(16,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.WEDNESDAY, LocalTime.of(8,30), LocalTime.of(15,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.THURSDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.FRIDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.SATURDAY, LocalTime.NOON, LocalTime.of(14,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.SUNDAY, null, null);
        companyWorkSchedule.setStartDay(LocalDate.of(2024, 3, 14).toString());
        companyWorkSchedule.setEndDay(LocalDate.of(2024, 7, 22).toString());
        employee1.setSchedule(companyWorkSchedule);
        WorkSchedule companyWorkSchedule2 = new WorkSchedule();
        companyWorkSchedule2.setWorkTime(DayOfWeek.MONDAY, LocalTime.NOON, LocalTime.of(15,30));
        companyWorkSchedule2.setWorkTime(DayOfWeek.TUESDAY, LocalTime.of(8,30), LocalTime.of(16,30));
        companyWorkSchedule2.setWorkTime(DayOfWeek.WEDNESDAY, LocalTime.of(8,30), LocalTime.of(15,30));
        companyWorkSchedule2.setWorkTime(DayOfWeek.THURSDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule2.setWorkTime(DayOfWeek.FRIDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule2.setWorkTime(DayOfWeek.SATURDAY, LocalTime.NOON, LocalTime.of(14,30));
        companyWorkSchedule2.setWorkTime(DayOfWeek.SUNDAY, LocalTime.NOON, LocalTime.of(15,30));
        companyWorkSchedule2.setStartDay(LocalDate.of(2024, 3, 14).toString());
        companyWorkSchedule2.setEndDay(LocalDate.of(2024, 7, 22).toString());
        employee1.setSchedule(companyWorkSchedule2);
        WorkSchedule companyWorkSchedule3 = new WorkSchedule();
        companyWorkSchedule3.setWorkTime(DayOfWeek.MONDAY, LocalTime.NOON, LocalTime.of(15,30));
        companyWorkSchedule3.setWorkTime(DayOfWeek.TUESDAY, LocalTime.of(8,30), LocalTime.of(16,30));
        companyWorkSchedule3.setWorkTime(DayOfWeek.WEDNESDAY, LocalTime.of(8,30), LocalTime.of(15,30));
        companyWorkSchedule3.setWorkTime(DayOfWeek.THURSDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule3.setWorkTime(DayOfWeek.FRIDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule3.setWorkTime(DayOfWeek.SATURDAY, LocalTime.NOON, LocalTime.of(14,30));
        companyWorkSchedule3.setWorkTime(DayOfWeek.SUNDAY, LocalTime.NOON, LocalTime.of(15,30));
        companyWorkSchedule3.setStartDay(LocalDate.of(2024, 3, 14).toString());
        companyWorkSchedule3.setEndDay(LocalDate.of(2024, 7, 22).toString());
        employee1.setSchedule(companyWorkSchedule3);
        employeesFake.add(employee1);
        employeesFake.add(employee2);
        employeesFake.add(employee3);
        employeesFake.add(employee4);
        FragmentTransition.to(EmployeeListFragment.newInstance(), getActivity(),
                true, R.id.scroll_employees_list, "employeesPage");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}