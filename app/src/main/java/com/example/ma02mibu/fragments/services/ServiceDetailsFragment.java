package com.example.ma02mibu.fragments.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.StringListAdapter;
import com.example.ma02mibu.databinding.FragmentServicesListBinding;
import com.example.ma02mibu.databinding.ServiceDetailsBinding;
import com.example.ma02mibu.model.Service;

import org.w3c.dom.Text;

public class ServiceDetailsFragment extends Fragment {

    private ServiceDetailsBinding binding;
    private static final String ARG_PARAM1 = "param1";

    private Service mService;
    public ServiceDetailsFragment() {
    }

    public static ServiceDetailsFragment newInstance(Service service) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, service);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mService = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ServiceDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView serviceName = binding.serviceNameDetail;
        TextView serviceDescription = binding.serviceDescriptionDetail;
        TextView serviceLocation = binding.locationServiceDetail;
        TextView serviceSpecificity = binding.specificityServiceDetail;
        TextView serviceDuration = binding.durationServiceDetail;
        TextView serviceHourPrice = binding.hourPriceServiceDetail;
        TextView serviceDiscount = binding.discountServiceDetail;
        TextView serviceTotalPrice = binding.totalPriceServiceDetail;
        TextView serviceReservationDeadline = binding.reservationServiceDetail;
        TextView serviceCancellation = binding.cancellationServiceDetail;
        ImageButton rightButton = binding.rightButtonServiceDetail;
        ImageButton leftButton = binding.leftButtonServiceDetail;
        ImageView imageView = binding.serviceImageDetail;
        handleRightButtonClick(rightButton, imageView, mService);
        handleLeftButtonClick(leftButton, imageView, mService);
        serviceName.setText(mService.getName());
        serviceDescription.setText(mService.getDescription());
        serviceLocation.setText(mService.getLocation());
        serviceSpecificity.setText(mService.getSpecificity());
        serviceDuration.setText(mService.getDuration());
        String priceByHour = mService.getPriceByHour() + "din";
        serviceHourPrice.setText(priceByHour);
        String discount = mService.getDiscount() + "%";
        serviceDiscount.setText(discount);
        serviceTotalPrice.setText(mService.getTotalPrice());
        serviceReservationDeadline.setText(mService.getReservationDeadlineText());
        serviceCancellation.setText(mService.getCancellationDeadlineText());
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView mRecyclerView = binding.eventTypeTagsServiceDetail;
        mRecyclerView.setLayoutManager(layoutManager);
        StringListAdapter listAdapter = new StringListAdapter(getActivity(), mService.getEventTypes());
        mRecyclerView.setAdapter(listAdapter);

        return root;
    }
    private void handleRightButtonClick(ImageButton rightButton, ImageView imageView, Service service){
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.setCurrentImageIndex(1);
                int image = service.getImages().get(service.getCurrentImageIndex());
                imageView.setImageResource(image);
            }
        });
    }

    private void handleLeftButtonClick(ImageButton leftButton, ImageView imageView, Service service){
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.setCurrentImageIndex(0);
                int image = service.getImages().get(service.getCurrentImageIndex());
                imageView.setImageResource(image);
            }
        });
    }
}
