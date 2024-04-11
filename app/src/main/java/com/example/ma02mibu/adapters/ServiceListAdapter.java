package com.example.ma02mibu.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.fragments.products.EditProductFragment;
import com.example.ma02mibu.fragments.services.ChooseServicesListFragment;
import com.example.ma02mibu.fragments.services.EditServiceFragment;
import com.example.ma02mibu.fragments.services.ServiceDetailsFragment;

import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class ServiceListAdapter extends ArrayAdapter<Service> {
    private ArrayList<Service> aServices;
    private FragmentActivity currFragActivity;
    Context context;
    private boolean isFromPackage;
    ChooseServicesListFragment currFragment;
    public ServiceListAdapter(Context context, ArrayList<Service> services, FragmentActivity fragmentActivity, boolean isFromPackage, ChooseServicesListFragment fragment){
        super(context, R.layout.services_card, services);
        this.context = context;
        aServices = services;
        currFragActivity = fragmentActivity;
        this.isFromPackage = isFromPackage;
        currFragment = fragment;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.services_card,
                    parent, false);
        }
        CheckBox checkBox = convertView.findViewById(R.id.choose_service_cb);
        ConstraintLayout layout = convertView.findViewById(R.id.service_card_item);
        ImageView imageView = convertView.findViewById(R.id.service_image);
        TextView productName = convertView.findViewById(R.id.service_name);
        TextView productDescription = convertView.findViewById(R.id.service_description);
        TextView category = convertView.findViewById(R.id.service_category);
        TextView subCategory = convertView.findViewById(R.id.service_subcategory);
        TextView duration = convertView.findViewById(R.id.service_duration);
        ImageButton rightButton = convertView.findViewById(R.id.right_button_service);
        ImageButton leftButton = convertView.findViewById(R.id.left_button_service);
        ImageButton menuButton = convertView.findViewById(R.id.more_button_service);
        handleRightButtonClick(rightButton, imageView, service);
        handleLeftButtonClick(leftButton, imageView, service);

        handleCardClick(layout, service);
        if(service != null){
            int image = service.getImages().get(service.getCurrentImageIndex());
            String heading = service.getName() + ", " + service.getLocation();
            imageView.setImageResource(image);
            productName.setText(heading);
            productDescription.setText(service.getDescription());
            category.setText(service.getCategory());
            subCategory.setText(service.getSubCategory());
            String durationText = "Duration: "+service.getDuration();
            duration.setText(durationText);
        }
        LinearLayoutManager layoutManager= new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView mRecyclerView = convertView.findViewById(R.id.event_type_tags_service);
        mRecyclerView.setLayoutManager(layoutManager);
        StringListAdapter listAdapter = new StringListAdapter(context, service.getEventTypes());
        mRecyclerView.setAdapter(listAdapter);
        if(isFromPackage){
            menuButton.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);
            handleServiceCheck(checkBox, service);
        }else {
            handleServiceMenuButtonClick(menuButton, service);
        }
        return convertView;
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

    private void handleCardClick(ConstraintLayout layout, Service service){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(ServiceDetailsFragment.newInstance(service), currFragActivity,
                        true, R.id.scroll_services_list, "ServicesDetailsPage");
            }
        });
    }
    private void handleServiceMenuButtonClick(ImageButton menuButton, Service service){
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), menuButton);
                popup.getMenuInflater()
                        .inflate(R.menu.pup_pop_up_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.edit){
                            FragmentTransition.to(EditServiceFragment.newInstance(service), currFragActivity,
                                    true, R.id.scroll_services_list, "editServicePage");
                        }else if(item.getItemId() == R.id.delete){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Delete service");
                            builder.setMessage("Are you sure?");
                            builder.setIcon(R.drawable.warning_icon);
                            builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                            builder.setPositiveButton("Yes", (dialog, id) -> {
                                //obrisi uslugu
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }
    private void handleServiceCheck(CheckBox cb, Service service){
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currFragment.serviceChosen(service.getId());
                } else {
                    currFragment.serviceUnChosen(service.getId());
                }
            }
        });
    }
}
