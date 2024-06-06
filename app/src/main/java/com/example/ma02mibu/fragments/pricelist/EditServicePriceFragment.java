package com.example.ma02mibu.fragments.pricelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.EditProductPriceFragmentBinding;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

public class EditServicePriceFragment extends Fragment {

    private EditProductPriceFragmentBinding binding;
    private Service mService;
    private static final String ARG_PARAM = "param";
    public static EditServicePriceFragment newInstance(Service service) {
        EditServicePriceFragment fragment = new EditServicePriceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, service);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditProductPriceFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView productName = binding.ProductName;
        productName.setText(mService.getName());
        EditText productPrice = binding.ProductPriceEdit;
        productPrice.setText(String.valueOf(mService.getPriceByHour()));
        EditText productDiscount = binding.ProductDiscountEdit;
        productDiscount.setText(String.valueOf(mService.getDiscount()));
        Button submitBtn = binding.submitButton;
        submitBtn.setOnClickListener(v -> editService());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mService = getArguments().getParcelable(ARG_PARAM);
        }
    }

    private void editService(){
        String price = binding.ProductPriceEdit.getText().toString();
        String discount = binding.ProductDiscountEdit.getText().toString();
        int priceInt = Integer.parseInt(price);
        int discountInt = Integer.parseInt(discount);
        mService.setPriceByHour(priceInt);
        mService.setDiscount(discountInt);
        CloudStoreUtil.updateService(mService);
        FragmentTransition.to(ServicesPricelistFragment.newInstance(), getActivity(),
                false, R.id.scroll_services_pricelist, "falsh");
    }
}
