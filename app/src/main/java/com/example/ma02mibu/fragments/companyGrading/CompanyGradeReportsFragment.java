package com.example.ma02mibu.fragments.companyGrading;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.CompanyGradeReportsAdapter;
import com.example.ma02mibu.adapters.CompanyGradesListAdapter;
import com.example.ma02mibu.model.CompanyGrade;
import com.example.ma02mibu.model.CompanyGradeReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyGradeReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyGradeReportsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private CompanyGradeReportsAdapter adapter;

    private FirebaseAuth auth;
    private ArrayList<CompanyGradeReport> companyGradeReports;

    public CompanyGradeReportsFragment() {
        // Required empty public constructor
    }

    public static CompanyGradeReportsFragment newInstance(String param1, String param2) {
        CompanyGradeReportsFragment fragment = new CompanyGradeReportsFragment();
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

        View view = inflater.inflate(R.layout.fragment_company_grade_reports, container, false);

        listView = view.findViewById(R.id.list_view_reports);

        loadGradeReports();
        return view;
    }

    private void loadGradeReports() {
        CloudStoreUtil.getCompanyGradeReportsList(new CloudStoreUtil.GradesReportsListCallback() {
            @Override
            public void onSuccess(ArrayList<CompanyGradeReport> itemList) {
                companyGradeReports = new ArrayList<>(itemList);
                adapter = new CompanyGradeReportsAdapter(getActivity(), companyGradeReports);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("Error fetching documents: " + e.getMessage());
            }
        });
    }
}