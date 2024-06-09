package com.example.ma02mibu.fragments.reporting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.OdProfilePageFragmentBinding;
import com.example.ma02mibu.databinding.ProfilePageFragmentBinding;
import com.example.ma02mibu.fragments.events.ExploreAndFilter;
import com.example.ma02mibu.fragments.serviceReservationsOverview.ServiceReservationsOrganizerOverviewFragment;
import com.example.ma02mibu.model.CompanyReport;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.ODReport;
import com.example.ma02mibu.model.OrganizerProfilePageData;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.ProfilePageData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class OrganizerProfilePageFragment extends Fragment {
    private OdProfilePageFragmentBinding binding;
    private OrganizerProfilePageData profileData;
    private EventOrganizer organizer;
    private boolean reportFormOpened = false;
    private static final String ARG_PARAM = "param";
    private FirebaseAuth auth;
    private String userId;
    private String userEmail;
    public static OrganizerProfilePageFragment newInstance(OrganizerProfilePageData data) {
        OrganizerProfilePageFragment fragment = new OrganizerProfilePageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, data);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OdProfilePageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView firstName = binding.firstNameTextView;
        TextView lastName = binding.lastNameTextView;
        TextView address = binding.addressText;
        TextView phoneNumber = binding.phoneNumberText;
        TextView emailText = binding.emailText;
        LinearLayout layout = binding.reportFormLayout;
        CloudStoreUtil.getEventOrganizerByEmail(profileData.getEmail(), new CloudStoreUtil.EventOrganizerCallback() {
            @Override
            public void onSuccess(EventOrganizer myItem) {
                organizer = myItem;
                firstName.setText(organizer.getName());
                lastName.setText(organizer.getSurname());
                address.setText(organizer.getAddressOfResidence());
                emailText.setText(organizer.getEmail());
                phoneNumber.setText(organizer.getPhoneNumber());
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
        Button reportBtn = binding.reportOdButton;
        reportBtn.setOnClickListener(v -> toggleReportForm(layout));
        Button submitBtn = binding.submitReport;
        submitBtn.setOnClickListener(v -> reportOD());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profileData = getArguments().getParcelable(ARG_PARAM);
        }
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
            userEmail = user.getEmail();
        }
    }
    private void toggleReportForm(LinearLayout layout){
        reportFormOpened = !reportFormOpened;
        if (reportFormOpened)
            layout.setVisibility(View.VISIBLE);
        else
            layout.setVisibility(View.GONE);
    }
    private void reportOD(){
        String reason = binding.reportReason.getText().toString();
        if(!reason.isEmpty()) {
            ODReport report = new ODReport(reason, organizer.getName(), organizer.getUserUID(), userId, new Date());
            report.setReporterName(userEmail);
            CloudStoreUtil.insertODReport(report);
            OurNotification not = new OurNotification("vejihot961@mfyax.com", "Report","New report for  " + organizer.getName(), "notRead");
            CloudStoreUtil.insertNotification(not);
            FragmentTransition.to(ServiceReservationsOrganizerOverviewFragment.newInstance("",""), getActivity(),
                    false, R.id.scroll_services_res_list_owner, "falsh");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
