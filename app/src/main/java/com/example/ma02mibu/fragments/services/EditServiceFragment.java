package com.example.ma02mibu.fragments.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.databinding.EditProductBinding;
import com.example.ma02mibu.databinding.EditServiceBinding;
import com.example.ma02mibu.fragments.products.EditProductFragment;
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
        Button switchPageButton = binding.switchPageButton;
        switchPageButton.setOnClickListener(v -> switchFormPages());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mService = getArguments().getParcelable(ARG_PARAM);
        }
    }


    private void switchFormPages(){
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
