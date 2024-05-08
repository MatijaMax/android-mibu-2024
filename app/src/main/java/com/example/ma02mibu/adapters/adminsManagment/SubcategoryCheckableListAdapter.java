package com.example.ma02mibu.adapters.adminsManagment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Subcategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SubcategoryCheckableListAdapter extends ArrayAdapter<Subcategory> {
    private ArrayList<Subcategory> aSubcategories;
    private ArrayList<Boolean> aChecked;
    public SubcategoryCheckableListAdapter(Context context, ArrayList<Subcategory> subcategories){
        super(context, R.layout.subcategory_checkable_card, subcategories);
        aSubcategories = subcategories;
        aChecked = new ArrayList<Boolean>();
        for(Subcategory n: aSubcategories){
            aChecked.add(false);
        }
    }

    @Override
    public int getCount() {
        return aSubcategories.size();
    }

    @Nullable
    @Override
    public Subcategory getItem(int position) {
        return aSubcategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Subcategory subCategory = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subcategory_checkable_card,
                    parent, false);
        }
        TextView subcategoryName = convertView.findViewById(R.id.subCategoryName);
        TextView subcategoryDescription = convertView.findViewById(R.id.subCategoryDescription);
        TextView subcategoryType = convertView.findViewById(R.id.subCategoryType);
        CheckBox subcategoryChecked = convertView.findViewById(R.id.checkbox);
        if(subCategory != null){
            subcategoryName.setText(subCategory.getName());
            subcategoryDescription.setText(subCategory.getDescription());
            subcategoryType.setText(subCategory.getType().toString());
            subcategoryChecked.setChecked(aChecked.get(position));
        }

        return convertView;
    }


    public void toggleCheckedWithView(int position, View view, ViewGroup parent){
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.subcategory_checkable_card,
                    parent, false);
        }
        CheckBox checkBox = view.findViewById(R.id.checkbox);
        checkBox.setChecked(!checkBox.isChecked());
    }

    public void toggleChecked(int position){
        aChecked.set(position, !aChecked.get(position));
    }

    public void check(int position){

    }
}
