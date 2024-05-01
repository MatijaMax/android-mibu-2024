package com.example.ma02mibu.fragments.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.EditProductBinding;
import com.example.ma02mibu.databinding.EditServiceBinding;
import com.example.ma02mibu.fragments.products.EditProductFragment;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

public class EditServiceFragment extends Fragment {
    private EditServiceBinding binding;
    LinearLayout part1;
    LinearLayout part2;
    int currentPage;
    private Service mService;
    private static final String ARG_PARAM = "param";
    public static EditServiceFragment newInstance(Service service) {
        EditServiceFragment fragment = new EditServiceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, service);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentPage = 0;
        binding = EditServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        part1 = binding.layoutPart1;
        part2 = binding.layoutPart2;
        EditText serviceName = binding.ServiceNameEdit;
        serviceName.setText(mService.getName());
        EditText serviceDescription = binding.ServiceDescriptionEdit;
        serviceDescription.setText(mService.getDescription());
        EditText specificity = binding.ServiceSpecificityEdit;
        specificity.setText(mService.getSpecificity());
        EditText reservationDeadline = binding.ReservationDeadlineEdit;
        reservationDeadline.setText(mService.getReservationDeadline());
        EditText cancellationDeadLine = binding.CancellationDeadlineEdit;
        cancellationDeadLine.setText(mService.getCancellationDeadline());
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
        Button switchPageButton = binding.switchPageButton;
        switchPageButton.setOnClickListener(v -> switchFormPages());
        Button submitBtn = binding.submitButtonService;
        submitBtn.setOnClickListener(v -> editProduct());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mService = getArguments().getParcelable(ARG_PARAM);
        }
    }



    private void editProduct(){
        String name = binding.ServiceNameEdit.getText().toString();
        String description = binding.ServiceDescriptionEdit.getText().toString();
        String price = binding.ServicePriceEdit.getText().toString();
        String reservationDeadline = binding.ReservationDeadlineEdit.getText().toString();
        String cancellationDeadline = binding.CancellationDeadlineEdit.getText().toString();
        String maxHour = binding.ServiceMaxDurationHours.getText().toString();
        String minHour = binding.ServiceMinDurationHours.getText().toString();
        String maxMin = binding.ServiceMaxDurationMinutes.getText().toString();
        String minMin = binding.ServiceMinDurationMinutes.getText().toString();
        String specificity = binding.ServiceSpecificityEdit.getText().toString();
        boolean visible = binding.checkBoxODAvailable.isChecked();
        boolean isAvailableToBuy = binding.checkBoxBuyAvailable.isChecked();
        boolean confirmAuto = binding.radioAutomatically.isChecked();
        int priceInt = Integer.parseInt(price);
        int maxHourInt = Integer.parseInt(maxHour);
        int minHourInt = Integer.parseInt(minHour);
        int maxMinInt = Integer.parseInt(maxMin);
        int minMinInt = Integer.parseInt(minMin);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
