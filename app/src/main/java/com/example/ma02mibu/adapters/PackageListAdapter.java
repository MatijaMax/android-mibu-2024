package com.example.ma02mibu.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Package;

import java.util.ArrayList;

public class PackageListAdapter extends ArrayAdapter<Package> {
    private ArrayList<Package> aPackages;
    private FragmentActivity currFragActivity;
    Context context;
    public PackageListAdapter(Context context, ArrayList<Package> packages, FragmentActivity fragmentActivity){
        super(context, R.layout.package_card, packages);
        this.context = context;
        aPackages = packages;
        currFragActivity = fragmentActivity;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.package_card,
                    parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.package_image);
        TextView packageName = convertView.findViewById(R.id.package_name);
        TextView packageDescription = convertView.findViewById(R.id.package_description);
        TextView category = convertView.findViewById(R.id.package_category);
        TextView price = convertView.findViewById(R.id.package_price);
        ImageButton rightButton = convertView.findViewById(R.id.right_button_package);
        ImageButton leftButton = convertView.findViewById(R.id.left_button_package);
        ImageButton menuButton = convertView.findViewById(R.id.more_button_package);
        handleRightButtonClick(rightButton, imageView, aPackage);
        handleLeftButtonClick(leftButton, imageView, aPackage);
        //handleProductMenuButtonClick(menuButton, product);
        if(aPackage != null){
            int image = aPackage.getImages().get(aPackage.getCurrentImageIndex());
            imageView.setImageResource(image);
            packageName.setText(aPackage.getName());
            packageDescription.setText(aPackage.getDescription());
            category.setText(aPackage.getCategory());
            price.setText(aPackage.getPrice());
        }
        LinearLayoutManager layoutManager= new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView mRecyclerView = convertView.findViewById(R.id.event_type_tags);
        mRecyclerView.setLayoutManager(layoutManager);
        StringListAdapter listAdapter = new StringListAdapter(context, new ArrayList<String>(aPackage.getEventTypes()));
        mRecyclerView.setAdapter(listAdapter);
        return convertView;
    }
    private void handleRightButtonClick(ImageButton rightButton, ImageView imageView, Package aPackage){
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aPackage.setCurrentImageIndex(1);
                int image = aPackage.getImages().get(aPackage.getCurrentImageIndex());
                imageView.setImageResource(image);
            }
        });
    }

    private void handleLeftButtonClick(ImageButton leftButton, ImageView imageView, Package aPackage){
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aPackage.setCurrentImageIndex(0);
                int image = aPackage.getImages().get(aPackage.getCurrentImageIndex());
                imageView.setImageResource(image);
            }
        });
    }/*
    private void handleProductMenuButtonClick(ImageButton menuButton, Product product){
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), menuButton);
                popup.getMenuInflater()
                        .inflate(R.menu.pup_pop_up_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.edit){
                            FragmentTransition.to(EditProductFragment.newInstance(product), currFragActivity,
                                    true, R.id.scroll_products_list, "editProductPage");
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }*/
}
