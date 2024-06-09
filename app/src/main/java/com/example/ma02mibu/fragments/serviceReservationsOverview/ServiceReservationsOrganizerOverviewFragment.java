package com.example.ma02mibu.fragments.serviceReservationsOverview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.ServiceReservationListAdapter;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.ServiceReservationDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceReservationsOrganizerOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceReservationsOrganizerOverviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private String selectTag;
    private ListView listView;
    private ArrayList<EmployeeReservation> employeeReservations;
    private ArrayList<ServiceReservationDTO> serviceReservationDTOS;
    private ArrayList<ServiceReservationDTO> serviceReservationDTOSfinal;

    public ServiceReservationsOrganizerOverviewFragment() {
        // Required empty public constructor
    }

    public static ServiceReservationsOrganizerOverviewFragment newInstance(String param1, String param2) {
        ServiceReservationsOrganizerOverviewFragment fragment = new ServiceReservationsOrganizerOverviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_service_reservations_organizer_overview, container, false);

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


        Button btnFilter = view.findViewById(R.id.btnSearch);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReservations();
            }
        });

        listView = view.findViewById(R.id.listView);


        loadReservations();


        return view;
    }

    private void loadReservations(){
        serviceReservationDTOS = new ArrayList<>();
        serviceReservationDTOSfinal = new ArrayList<>();
        CloudStoreUtil.getServieReservationsList(new CloudStoreUtil.ServiceReservationsListCallback() {
            @Override
            public void onSuccess(ArrayList<EmployeeReservation> itemList) {
                employeeReservations = new ArrayList<>(itemList);
                employeeReservations.removeIf(r -> !r.getEventOrganizerEmail().equals(currentUser.getEmail()));
                System.out.println(employeeReservations.size() + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                for (EmployeeReservation er: employeeReservations) {
                    serviceReservationDTOS.add(new ServiceReservationDTO(er));
                }
                for (ServiceReservationDTO sr: serviceReservationDTOS) {
                    CloudStoreUtil.getEmployeeByEmail(sr.getEmployeeEmail(), new CloudStoreUtil.EmployeeByEmailCallback() {
                        @Override
                        public void onSuccess(Employee item) {
                            sr.setEmployeeFirstName(item.getFirstName());
                            sr.setEmployeeLastName(item.getLastName());
                            System.out.println(sr.getEmployeeFirstName() + "BBBBBBBBBBBB");
                            CloudStoreUtil.getOrganizerByEmail(sr.getEventOrganizerEmail(), new CloudStoreUtil.OrganizerByEmailCallback() {
                                @Override
                                public void onSuccess(EventOrganizer item) {
                                    sr.setEventOrganizerFirstName(item.getName());
                                    sr.setEventOrganizerLastName(item.getSurname());
                                    System.out.println(sr.getEventOrganizerFirstName() + "CCCCCCCCCCCCCC" + sr.getEmployeeFirstName());
                                    CloudStoreUtil.getServiceWithRefId(sr.getServiceRefId(), new CloudStoreUtil.ServiceByRefIdCallback() {
                                        @Override
                                        public void onSuccess(Service item) {
                                            sr.setServiceName(item.getName());
                                            sr.setCancellationDeadline(item.getCancellationDeadline());
                                            sr.setConfirmAutomatically(item.isConfirmAutomatically());
                                            System.out.println(sr.getServiceName() + "DDDDDDDDDDDD" + sr.getEmployeeFirstName());
                                            serviceReservationDTOSfinal.add(sr);
                                            if(!selectTag.equals("All")){
                                                serviceReservationDTOSfinal.removeIf(sr -> !(sr.getStatus().toString().equals(selectTag)));
                                            }
                                            ServiceReservationListAdapter adapter = new ServiceReservationListAdapter(getActivity(), serviceReservationDTOSfinal, getActivity());
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
}