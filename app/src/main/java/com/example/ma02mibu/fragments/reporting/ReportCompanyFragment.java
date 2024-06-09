package com.example.ma02mibu.fragments.reporting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.ReportCompanyFragmentBinding;
import com.example.ma02mibu.fragments.events.ExploreAndFilter;
import com.example.ma02mibu.model.CompanyReport;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.ProductDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class ReportCompanyFragment extends Fragment {
    private ReportCompanyFragmentBinding binding;
    private ProductDAO aProduct;
    private Owner owner;
    private EventOrganizer user;
    private FirebaseAuth auth;
    private String userId;
    private static final String ARG_PARAM = "param";
    public static ReportCompanyFragment newInstance(ProductDAO product) {
        ReportCompanyFragment fragment = new ReportCompanyFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, product);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ReportCompanyFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        CloudStoreUtil.getOwner(aProduct.getOwnerUuid(), new CloudStoreUtil.OwnerCallback() {
            @Override
            public void onSuccess(Owner myItem) {
                owner = myItem;
                TextView productName = binding.CompanyName;
                String header = "Reporting "+owner.getMyCompany().getName()+" company";
                productName.setText(header);
                TextView ownerName = binding.OwnerName;
                String ownerHeader = "Owner: "+owner.getName();
                ownerName.setText(ownerHeader);
            }
            @Override
            public void onFailure(Exception e) {
                owner = null;
            }
        });
        CloudStoreUtil.getEventOrganizer(userId, new CloudStoreUtil.EventOrganizerCallback() {
            @Override
            public void onSuccess(EventOrganizer myItem) {
                user = myItem;
            }
            @Override
            public void onFailure(Exception e) {
                user = null;
            }
        });
        Button submitBtn = binding.submitButton;
        submitBtn.setOnClickListener(v -> reportCompany());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            aProduct = getArguments().getParcelable(ARG_PARAM);
        }
    }

    private void reportCompany(){
        String reason = binding.reportReason.getText().toString();
        if(!reason.isEmpty()) {
            CompanyReport report = new CompanyReport(reason, owner.getMyCompany().getName(), owner.getUserUID(), userId, new Date());
            report.setReporterName(user.getName());
            CloudStoreUtil.insertCompanyReport(report);
            OurNotification not = new OurNotification("vejihot961@mfyax.com", "Report","New report for  " + owner.getMyCompany().getName() + " company", "notRead");
            CloudStoreUtil.insertNotification(not);
            FragmentTransition.to(ExploreAndFilter.newInstance(null), getActivity(),
                    false, R.id.scroll_products_list2, "falsh");
        }

    }
}
