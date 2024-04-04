package com.example.ma02mibu.adapters.adminsManagment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.SubCategoryRequest;

import java.util.ArrayList;

public class SubcategoryRequestListAdapter extends ArrayAdapter<SubCategoryRequest> {
    private ArrayList<SubCategoryRequest> aSubCategoryRequests;
    public SubcategoryRequestListAdapter(Context context, ArrayList<SubCategoryRequest> subCategoryRequests){
        super(context, R.layout.subcategory_request_card, subCategoryRequests);
        aSubCategoryRequests = subCategoryRequests;
    }

    @Override
    public int getCount() {
        return aSubCategoryRequests.size();
    }

    @Nullable
    @Override
    public SubCategoryRequest getItem(int position) {
        return aSubCategoryRequests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SubCategoryRequest subCategoryRequest = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subcategory_request_card,
                    parent, false);
        }
        TextView subcategoryRequestName = convertView.findViewById(R.id.subcategoryRequestName);
        TextView subcategoryRequestDescription = convertView.findViewById(R.id.subcategoryRequestDescription);
        TextView subcategoryRequestType = convertView.findViewById(R.id.subcategoryRequestType);
        if(subCategoryRequest != null){
            subcategoryRequestName.setText(subCategoryRequest.getName());
            subcategoryRequestDescription.setText(subCategoryRequest.getDescription());
            subcategoryRequestType.setText(subCategoryRequest.getType().toString());
        }

        return convertView;
    }
}