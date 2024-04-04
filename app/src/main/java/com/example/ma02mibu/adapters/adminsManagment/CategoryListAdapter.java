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
import com.example.ma02mibu.model.Category;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private ArrayList<Category> aCategories;
    public CategoryListAdapter(Context context, ArrayList<Category> categories){
        super(context, R.layout.category_managment_card, categories);
        aCategories = categories;
    }

    @Override
    public int getCount() {
        return aCategories.size();
    }

    @Nullable
    @Override
    public Category getItem(int position) {
        return aCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Category category = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_managment_card,
                    parent, false);
        }
        TextView categoryName = convertView.findViewById(R.id.categoryName);
        TextView categoryDescription = convertView.findViewById(R.id.categoryDescription);
        if(category != null){
            categoryName.setText(category.getName());
            categoryDescription.setText(category.getDescription());
        }

        return convertView;
    }
}
