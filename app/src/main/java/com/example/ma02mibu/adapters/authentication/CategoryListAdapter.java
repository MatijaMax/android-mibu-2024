package com.example.ma02mibu.adapters.authentication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private ArrayList<Category> aCategories;
    public CategoryListAdapter(Context context, ArrayList<Category> categories){
        super(context, R.layout.category_card, categories);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_card,
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

    public void toggleCheckedWithView(int position, View view, ViewGroup parent){
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.category_card,
                    parent, false);
        }

        CheckBox checkBox = view.findViewById(R.id.checkbox);
        checkBox.setChecked(!checkBox.isChecked());
    }
}
