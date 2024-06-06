package com.example.ma02mibu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.fragments.pricelist.EditProductPriceFragment;
import com.example.ma02mibu.fragments.pricelist.EditServicePriceFragment;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class ServicesPricelistAdapter extends ArrayAdapter<Service> {
    Context context;
    private ArrayList<Service> aServices;
    private FragmentActivity activity;
    private boolean isOwner;
    public ServicesPricelistAdapter(Context context, ArrayList<Service> services, FragmentActivity activity, boolean isOwner){
        super(context, R.layout.product_pricelist_item, services);
        this.context = context;
        aServices = services;
        this.activity = activity;
        this.isOwner = isOwner;
    }
    @Override
    public int getCount() {
        return aServices.size();
    }

    @Nullable
    @Override
    public Service getItem(int position) {
        return aServices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Service service = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_pricelist_item,
                    parent, false);
        }
        TextView productName = convertView.findViewById(R.id.productName);
        TextView productSerialNumber = convertView.findViewById(R.id.serialNumber);
        TextView oldPrice = convertView.findViewById(R.id.price);
        TextView newPrice = convertView.findViewById(R.id.priceNew);
        TextView discount = convertView.findViewById(R.id.discount);
        Button changePriceButton = convertView.findViewById(R.id.changeProductPrice);
        if(isOwner)
            changePriceButton.setOnClickListener(v -> openEditPriceFragment(service));
        else
            changePriceButton.setVisibility(View.GONE);
        if(service != null){
            productName.setText(service.getName());
            productSerialNumber.setText(String.valueOf(position));
            oldPrice.setText(String.valueOf(service.getPriceByHour()));
            String discountText = service.getDiscount() + "% off";
            discount.setText(discountText);
            String currentPrice = "Current price: "+ service.getDiscountedHourPrice();
            newPrice.setText(currentPrice);
        }
        return convertView;
    }
    private void openEditPriceFragment(Service service){
        FragmentTransition.to(EditServicePriceFragment.newInstance(service), activity,
                false, R.id.scroll_services_pricelist, "edit_service_price");
    }
}
