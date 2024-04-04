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
import com.example.ma02mibu.model.SubCategory;

import java.util.ArrayList;

public class SubcategoryCheckableListAdapter extends ArrayAdapter<SubCategory> {
    private ArrayList<SubCategory> aSubCategories;
    public SubcategoryCheckableListAdapter(Context context, ArrayList<SubCategory> subCategories){
        super(context, R.layout.subcategory_checkable_card, subCategories);
        aSubCategories = subCategories;
    }

    @Override
    public int getCount() {
        return aSubCategories.size();
    }

    @Nullable
    @Override
    public SubCategory getItem(int position) {
        return aSubCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SubCategory subCategory = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subcategory_checkable_card,
                    parent, false);
        }
        TextView subCategoryName = convertView.findViewById(R.id.subCategoryName);
        TextView subCategoryDescription = convertView.findViewById(R.id.subCategoryDescription);
        TextView subCategoryType = convertView.findViewById(R.id.subCategoryType);
        if(subCategory != null){
            subCategoryName.setText(subCategory.getName());
            subCategoryDescription.setText(subCategory.getDescription());
            subCategoryType.setText(subCategory.getType().toString());
        }

        return convertView;
    }
}
