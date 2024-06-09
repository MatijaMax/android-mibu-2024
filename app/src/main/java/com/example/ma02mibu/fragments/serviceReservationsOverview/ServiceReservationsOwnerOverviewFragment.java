package com.example.ma02mibu.fragments.serviceReservationsOverview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.EmployeeListAdapter;
import com.example.ma02mibu.adapters.ServiceReservationListAdapter;
import com.example.ma02mibu.adapters.ServiceReservationOwnerListAdapter;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.ServiceReservationDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ServiceReservationsOwnerOverviewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private ListView listView;
    private EditText searchData;
    private String selectTag;
    private ArrayList<Employee> employees;
    private ArrayList<EmployeeReservation> employeeReservations;
    private ArrayList<ServiceReservationDTO> serviceReservationDTOS;
    private ArrayList<ServiceReservationDTO> serviceReservationDTOSfinal;

    public ServiceReservationsOwnerOverviewFragment() {
        // Required empty public constructor
    }

    public static ServiceReservationsOwnerOverviewFragment newInstance(String param1, String param2) {
        ServiceReservationsOwnerOverviewFragment fragment = new ServiceReservationsOwnerOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_reservations_owner_overview, container, false);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        Spinner spinner = view.findViewById(R.id.spinner_options);

        List<String> options = new ArrayList<>();
        options.add("All");
        options.add("New");
        options.add("CanceledByPUP");
        options.add("CanceledByOD");
        options.add("CanceledByAdmin");
        options.add("Accepted");
        options.add("Finished");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, options);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectTag = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        searchData = view.findViewById(R.id.textSearch);
        Button btnFilter = view.findViewById(R.id.btnSearch);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReservations();
            }
        });

        listView = view.findViewById(R.id.listViewOwnerReservations);


        loadReservations();


        return view;
    }

    private void loadReservations(){
        serviceReservationDTOS = new ArrayList<>();
        serviceReservationDTOSfinal = new ArrayList<>();
        CloudStoreUtil.getEmployeesList(currentUser.getUid(), new CloudStoreUtil.EmployeesListCallback() {
            @Override
            public void onSuccess(ArrayList<Employee> itemList) {
                employees = new ArrayList<>(itemList);
                CloudStoreUtil.getServieReservationsList(new CloudStoreUtil.ServiceReservationsListCallback() {
                    @Override
                    public void onSuccess(ArrayList<EmployeeReservation> itemList) {
                        employeeReservations = new ArrayList<>(itemList);
                        employeeReservations.removeIf(r -> !r.isOwnersRes(employees));

                        employeeReservations = new ArrayList<>(employeeReservations);

                        for (EmployeeReservation er: employeeReservations) {
                            serviceReservationDTOS.add(new ServiceReservationDTO(er));
                        }
                        for (ServiceReservationDTO sr: serviceReservationDTOS) {
                            CloudStoreUtil.getEmployeeByEmail(sr.getEmployeeEmail(), new CloudStoreUtil.EmployeeByEmailCallback() {
                                @Override
                                public void onSuccess(Employee item) {
                                    sr.setEmployeeFirstName(item.getFirstName());
                                    sr.setEmployeeLastName(item.getLastName());

                                    CloudStoreUtil.getOrganizerByEmail(sr.getEventOrganizerEmail(), new CloudStoreUtil.OrganizerByEmailCallback() {
                                        @Override
                                        public void onSuccess(EventOrganizer item) {
                                            sr.setEventOrganizerFirstName(item.getName());
                                            sr.setEventOrganizerLastName(item.getSurname());
                                            CloudStoreUtil.getServiceWithRefId(sr.getServiceRefId(), new CloudStoreUtil.ServiceByRefIdCallback() {
                                                @Override
                                                public void onSuccess(Service item) {
                                                    sr.setServiceName(item.getName());
                                                    sr.setCancellationDeadline(item.getCancellationDeadline());
                                                    sr.setConfirmAutomatically(item.isConfirmAutomatically());
                                                    serviceReservationDTOSfinal.add(sr);
                                                    String s = searchData.getText().toString().toLowerCase();
                                                    if(!s.isEmpty()){
                                                        serviceReservationDTOSfinal.removeIf(sr -> !(sr.getEmployeeFirstName().toLowerCase().contains(s)
                                                                || sr.getEmployeeLastName().toLowerCase().contains(s) || sr.getServiceName().toLowerCase().contains(s)
                                                                || sr.getEventOrganizerFirstName().toLowerCase().contains(s) || sr.getEventOrganizerLastName().toLowerCase().contains(s)));
                                                    }
                                                    if(!selectTag.equals("All")){
                                                        serviceReservationDTOSfinal.removeIf(sr -> !(sr.getStatus().toString().equals(selectTag)));
                                                    }
                                                    ServiceReservationOwnerListAdapter adapter = new ServiceReservationOwnerListAdapter(getActivity(), serviceReservationDTOSfinal, getActivity());
                                                    listView.setAdapter(adapter);
                                                }

                                                @Override
                                                public void onFailure(Exception e) {
                                                    System.err.println("Error fetching documents: " + e.getMessage());
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            System.err.println("Error fetching documents: " + e.getMessage());
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println("Error fetching documents: " + e.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Error fetching documents: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("Error fetching documents: " + e.getMessage());
            }
        });

    }
}