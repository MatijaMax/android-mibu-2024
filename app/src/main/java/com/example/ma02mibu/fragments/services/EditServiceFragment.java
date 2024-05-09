package com.example.ma02mibu.fragments.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.EditServiceBinding;
import com.example.ma02mibu.model.Deadline;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EmployeeInService;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class EditServiceFragment extends Fragment {
    private EditServiceBinding binding;
    LinearLayout part1;
    LinearLayout part2;
    int currentPage;
    ArrayList<EmployeeInService> employees = new ArrayList<>();
    private Service mService;
    private FirebaseAuth auth;
    private String ownerId;
    private ArrayList<String> deadlineFormats;
    private static final String ARG_PARAM = "param";
    public static EditServiceFragment newInstance(Service service) {
        EditServiceFragment fragment = new EditServiceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, service);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ownerId = user.getUid();
        currentPage = 0;
        binding = EditServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        part1 = binding.layoutPart1;
        part2 = binding.layoutPart2;
        binding.resDeadlineFormatEdit.setAdapter(setDateFormatAdapter());
        binding.resCancellationFormatEdit.setAdapter(setDateFormatAdapter());
        EditText serviceName = binding.ServiceNameEdit;
        serviceName.setText(mService.getName());
        EditText serviceDescription = binding.ServiceDescriptionEdit;
        serviceDescription.setText(mService.getDescription());
        EditText specificity = binding.ServiceSpecificityEdit;
        specificity.setText(mService.getSpecificity());
        EditText reservationDeadline = binding.ReservationDeadlineEdit;
        reservationDeadline.setText(String.valueOf(mService.getReservationDeadline().getNumber()));
        EditText cancellationDeadLine = binding.CancellationDeadlineEdit;
        cancellationDeadLine.setText(String.valueOf(mService.getCancellationDeadline().getNumber()));
        setSpinnerSelection();
        EditText priceByHour = binding.ServicePriceEdit;
        priceByHour.setText(String.valueOf(mService.getPriceByHour()));
        EditText minHour = binding.ServiceMinDurationHours;
        minHour.setText(String.valueOf(mService.getMinHourDuration()));
        EditText maxHour = binding.ServiceMaxDurationHours;
        maxHour.setText(String.valueOf(mService.getMaxHourDuration()));
        EditText minMin = binding.ServiceMinDurationMinutes;
        minMin.setText(String.valueOf(mService.getMinMinutesDuration()));
        EditText maxMin = binding.ServiceMaxDurationMinutes;
        maxMin.setText(String.valueOf(mService.getMaxMinutesDuration()));
        EditText discount = binding.ServiceDiscountEdit;
        discount.setText(String.valueOf(mService.getDiscount()));
        CheckBox visibleCheckBox = binding.checkBoxODAvailable;
        visibleCheckBox.setChecked(mService.isVisible());
        CheckBox buyAvailableCheckBox = binding.checkBoxBuyAvailable;
        buyAvailableCheckBox.setChecked(mService.isAvailableToBuy());
        if(mService.isConfirmAutomatically()){
            RadioButton rb = binding.radioAutomatically;
            rb.setChecked(true);
        }else{
            RadioButton rb = binding.radioManually;
            rb.setChecked(true);
        }
        getEmployees(ownerId);
        getEventTypes();
        Button switchPageButton = binding.switchPageButton;
        switchPageButton.setOnClickListener(v -> switchFormPages());
        Button submitBtn = binding.submitButtonService;
        submitBtn.setOnClickListener(v -> editService());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mService = getArguments().getParcelable(ARG_PARAM);
        }
    }



    private void editService(){
        ListView listView = binding.employeesListViewEdit;
        ArrayList<EmployeeInService> checkedEmployees = new ArrayList<>();
        int cnt = listView.getAdapter().getCount();
        for(int i=0; i<cnt; i++){
            if(listView.isItemChecked(i))
                checkedEmployees.add(employees.get(i));
        }

        ListView listViewEventTypes = binding.eventTypesListServiceEdit;
        cnt = listViewEventTypes.getAdapter().getCount();
        ArrayList<String> eventTypes = new ArrayList<>();
        for(int i=0; i<cnt; i++){
            if(listViewEventTypes.isItemChecked(i))
                eventTypes.add(listViewEventTypes.getItemAtPosition(i).toString());
        }

        String name = binding.ServiceNameEdit.getText().toString();
        String description = binding.ServiceDescriptionEdit.getText().toString();
        String price = binding.ServicePriceEdit.getText().toString();
        String reservationDeadlineNum = binding.ReservationDeadlineEdit.getText().toString();
        String cancellationDeadlineNum = binding.CancellationDeadlineEdit.getText().toString();
        String reservationDeadlineFormat = binding.resDeadlineFormatEdit.getSelectedItem().toString();
        String cancellationDeadlineFormat = binding.resCancellationFormatEdit.getSelectedItem().toString();
        String maxHour = binding.ServiceMaxDurationHours.getText().toString();
        String minHour = binding.ServiceMinDurationHours.getText().toString();
        String maxMin = binding.ServiceMaxDurationMinutes.getText().toString();
        String minMin = binding.ServiceMinDurationMinutes.getText().toString();
        String specificity = binding.ServiceSpecificityEdit.getText().toString();
        String discount = binding.ServiceDiscountEdit.getText().toString();
        boolean visible = binding.checkBoxODAvailable.isChecked();
        boolean isAvailableToBuy = binding.checkBoxBuyAvailable.isChecked();
        boolean confirmAuto = binding.radioAutomatically.isChecked();
        int priceInt = Integer.parseInt(price);
        int maxHourInt = Integer.parseInt(maxHour);
        int minHourInt = Integer.parseInt(minHour);
        int maxMinInt = Integer.parseInt(maxMin);
        int minMinInt = Integer.parseInt(minMin);
        int discountInt = Integer.parseInt(discount);
        Deadline reservationDeadline = new Deadline(reservationDeadlineFormat, Integer.parseInt(reservationDeadlineNum));
        Deadline cancellationDeadline = new Deadline(cancellationDeadlineFormat, Integer.parseInt(cancellationDeadlineNum));
        mService.setName(name);
        mService.setDescription(description);
        mService.setPriceByHour(priceInt);
        mService.setMaxHourDuration(maxHourInt);
        mService.setMinHourDuration(minHourInt);
        mService.setMinMinutesDuration(minMinInt);
        mService.setMaxMinutesDuration(maxMinInt);
        mService.setSpecificity(specificity);
        mService.setCancellationDeadline(cancellationDeadline);
        mService.setReservationDeadline(reservationDeadline);
        mService.setVisible(visible);
        mService.setAvailableToBuy(isAvailableToBuy);
        mService.setConfirmAutomatically(confirmAuto);
        mService.setDiscount(discountInt);
        mService.setPersons(checkedEmployees);
        mService.setEventTypes(eventTypes);
        CloudStoreUtil.updateService(mService);
        FragmentTransition.to(ServicesListFragment.newInstance(), getActivity(),
                false, R.id.scroll_services_list, "falsh");
    }

    private void switchFormPages(){
        Button tv = binding.switchPageButton;
        if(currentPage == 0) {
            part1.setVisibility(View.GONE);
            part2.setVisibility(View.VISIBLE);
            currentPage = 1;
        }
        else{
            part1.setVisibility(View.VISIBLE);
            part2.setVisibility(View.GONE);
            currentPage = 0;
        }
        String text = "Page " + (currentPage+1) + "/2";
        tv.setText(text);
    }

    private ArrayAdapter<String> setDateFormatAdapter(){
        deadlineFormats = new ArrayList<>();
        deadlineFormats.add("days");
        deadlineFormats.add("months");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, deadlineFormats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void setSpinnerSelection(){
        if(mService.getReservationDeadline().getDateFormat().equals("days")){
            binding.resDeadlineFormatEdit.setSelection(0);
        }else{
            binding.resDeadlineFormatEdit.setSelection(1);
        }
        if(mService.getCancellationDeadline().getDateFormat().equals("days")){
            binding.resCancellationFormatEdit.setSelection(0);
        }else{
            binding.resCancellationFormatEdit.setSelection(1);
        }
    }

    private void getEmployees(String ownerRefId){
        CloudStoreUtil.getEmployeesList(ownerRefId, new CloudStoreUtil.EmployeesListCallback() {
            @Override
            public void onSuccess(ArrayList<Employee> itemList) {
                // Handle the retrieved list of items (e.g., display them in UI)
                ArrayList<Employee> employeesDB = new ArrayList<>(itemList);
                for(Employee e: employeesDB){
                    employees.add(new EmployeeInService(e.getEmail(), e.getFirstName(), e.getLastName()));
                }

                ListView listView = binding.employeesListViewEdit;
                ArrayList<String> employeesInList = new ArrayList<>();
                for (EmployeeInService e: employees)
                    employeesInList.add(e.getFirstName() + " " + e.getLastName());
                ArrayAdapter<String> employeesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, employeesInList);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(employeesAdapter);
                for(EmployeeInService e: mService.getPersons()){
                    for(int i=0; i<employees.size(); i++){
                        if(employees.get(i).getEmail().equals(e.getEmail())){
                            binding.employeesListViewEdit.setItemChecked(i, true);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error fetching documents: " + e.getMessage());
            }
        });
    }


    private void getEventTypes(){
        CloudStoreUtil.selectEventTypes(result -> {
            ListView listView = binding.eventTypesListServiceEdit;
            ArrayList<String> eventTypes = new ArrayList<>();
            for (EventType e: result)
                if(e.getStatus() == EventType.EVENTTYPESTATUS.ACTIVE)
                    eventTypes.add(e.getName());
            ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, eventTypes);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setAdapter(eventTypeAdapter);
            for(String s: mService.getEventTypes()){
                for(int i=0; i<eventTypes.size(); i++){
                    if(eventTypes.get(i).equals(s)){
                        binding.eventTypesListServiceEdit.setItemChecked(i, true);
                    }
                }
            }

        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
