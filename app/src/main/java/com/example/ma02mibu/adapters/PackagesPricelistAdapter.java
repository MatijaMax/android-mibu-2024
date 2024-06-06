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
import com.example.ma02mibu.fragments.pricelist.EditPackagePriceFragment;
import com.example.ma02mibu.fragments.pricelist.EditServicePriceFragment;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class PackagesPricelistAdapter extends ArrayAdapter<Package> {
    Context context;
    private ArrayList<Package> aPackages;
    private FragmentActivity activity;
    private boolean isOwner;
    public PackagesPricelistAdapter(Context context, ArrayList<Package> packages, FragmentActivity activity, boolean isOwner){
        super(context, R.layout.product_pricelist_item, packages);
        this.context = context;
        aPackages = packages;
        this.activity = activity;
        this.isOwner = isOwner;
    }
    @Override
    public int getCount() {
        return aPackages.size();
    }

    @Nullable
    @Override
    public Package getItem(int position) {
        return aPackages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Package aPackage = getItem(position);
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
            changePriceButton.setOnClickListener(v -> openEditPriceFragment(aPackage));
        else
            changePriceButton.setVisibility(View.GONE);
        if(aPackage != null){
            productName.setText(aPackage.getName());
            productSerialNumber.setText(String.valueOf(position));
            String price = aPackage.getMinPrice() + "-" +aPackage.getMaxPrice() + " din";
            oldPrice.setText(price);
            String discountText = aPackage.getDiscount() + "% off";
            discount.setText(discountText);
            String currentPrice = "Current price: "+ aPackage.getPrice();
            newPrice.setText(currentPrice);
        }
        return convertView;
    }
    private void openEditPriceFragment(Package aPackage){
        FragmentTransition.to(EditPackagePriceFragment.newInstance(aPackage), activity,
                false, R.id.scroll_packages_pricelist, "edit_package_price");
    }
}
