package com.example.ma02mibu.fragments.events;


import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.reporting.ReportCompanyFragment;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.ProductDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MaxCompanyFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_PARAM3= "param3";

    private ProductDAO product;

    private Owner currentOwner;

    private String ownerId;

    private Boolean isFav;


    private FirebaseAuth auth;

    private FirebaseUser currentUser;

    public static MaxCompanyFragment newInstance(ProductDAO product, String param2, Boolean param3) {
        MaxCompanyFragment fragment = new MaxCompanyFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, product);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PARAM1);
            ownerId = getArguments().getString(ARG_PARAM2);
            isFav = getArguments().getBoolean(ARG_PARAM3);
            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadOwner(ownerId);
        return inflater.inflate(R.layout.fragment_company_max, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvCompanyName = view.findViewById(R.id.tv_company_name);
        TextView tvCompanyEmail = view.findViewById(R.id.tv_company_email);
        TextView tvCompanyAddress = view.findViewById(R.id.tv_company_address);
        TextView tvCompanyPhone = view.findViewById(R.id.tv_company_phone);
        TextView tvCompanyDescription = view.findViewById(R.id.tv_company_description);
        TextView tvWorkSchedule = view.findViewById(R.id.tv_work_schedule);

        ListView lvRatingsComments = view.findViewById(R.id.lv_ratings_comments);
        Button btnReportCompany = view.findViewById(R.id.btn_report_company);

        if(currentOwner != null){
            tvCompanyName.setText(currentOwner.getMyCompany().getName());
            tvCompanyEmail.setText(currentOwner.getMyCompany().getEmail());
            tvCompanyAddress.setText(currentOwner.getMyCompany().getAddress());
            tvCompanyPhone.setText(currentOwner.getMyCompany().getPhoneNumber());
            tvCompanyDescription.setText(currentOwner.getMyCompany().getDescription());
            tvWorkSchedule.setText("Work Schedule: 9 AM - 5 PM");
        }
        tvCompanyName.setText("Example Company");
        tvCompanyEmail.setText("Email: example@example.com");
        tvCompanyAddress.setText("Address: 123 Example Street");
        tvCompanyPhone.setText("Phone: (123) 456-7890");
        tvCompanyDescription.setText("Description: This is an example company.");
        tvWorkSchedule.setText("Work Schedule: 9 AM - 5 PM");

        btnReportCompany.setOnClickListener(v -> {
            FragmentTransition.to(ReportCompanyFragment.newInstance(product), getActivity(),
                            true, R.id.scroll_products_list2, "report_form");
        });

    }


    private void loadOwner(String ownerRefId) {
        CloudStoreUtil.getOwner(ownerRefId, new CloudStoreUtil.OwnerCallback() {
            @Override
            public void onSuccess(Owner myItem) {
                currentOwner = myItem;
            }
            @Override
            public void onFailure(Exception e) {
                System.err.println("Error fetching owner document: " + e.getMessage());
            }
        });
    }
}


