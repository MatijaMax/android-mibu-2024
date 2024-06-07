package com.example.ma02mibu.fragments.companyGrading;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.CompanyGradesListAdapter;
import com.example.ma02mibu.adapters.EmployeeListAdapter;
import com.example.ma02mibu.databinding.FragmentEmployeeListBinding;
import com.example.ma02mibu.model.CompanyGrade;
import com.example.ma02mibu.model.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private ArrayList<CompanyGrade> companyGrades;
    private String ownerRefId;

    public CompanyProfileFragment() {
        // Required empty public constructor
    }
    private ListView listView;
    private CompanyGradesListAdapter adapter;
    public static CompanyProfileFragment newInstance(String param1, String param2) {
        CompanyProfileFragment fragment = new CompanyProfileFragment();
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
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            ownerRefId = user.getUid();
        }
        View view = inflater.inflate(R.layout.fragment_company_profile, container, false);

        listView = view.findViewById(R.id.list_view);

//        ArrayList<CompanyGrade> data = new ArrayList<>();
//        data.add(new CompanyGrade(90, "Good job!", "12345",  new Date(), "organizer@example.com"));
//        data.add(new CompanyGrade(80, "Could be better", "54321", new Date(), "organizer2@example.com"));


        loadGrades();

        return view;
    }

    private void loadGrades() {
        CloudStoreUtil.getCompanyGradesList(ownerRefId, new CloudStoreUtil.GradesListCallback() {
            @Override
            public void onSuccess(ArrayList<CompanyGrade> itemList) {
                // Handle the retrieved list of items (e.g., display them in UI)
                companyGrades = new ArrayList<>(itemList);
                adapter = new CompanyGradesListAdapter(getActivity(), companyGrades);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error fetching documents: " + e.getMessage());
            }
        });
    }
}