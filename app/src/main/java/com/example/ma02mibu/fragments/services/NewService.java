package com.example.ma02mibu.fragments.services;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.NewProductBinding;
import com.example.ma02mibu.databinding.NewServiceBinding;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;
import java.util.UUID;


public class NewService extends Fragment {

    int currentPage;
    private static final int PICK_IMAGES_REQUEST = 1;
    private LinearLayout imageContainer;
    private ArrayList<String> categories;
    private ArrayList<String> subCategories;
    LinearLayout part1;
    LinearLayout part2;
    LinearLayout part3;
    ArrayList<String> employees;
    NewServiceBinding binding;
    public static NewService newInstance() {
        return new NewService();
    }
    //tutorijal za rad sa listview-om i biranje iz listview-a: https://www.youtube.com/watch?v=l3BCsSUZoNE
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentPage = 0;
        binding = NewServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        part1 = binding.layoutPart1;
        part2 = binding.layoutPart2;
        part3 = binding.layoutPart3;
        imageContainer = binding.imageContainerService;
        ImageButton removeButton = binding.removeImagesButton;
        removeButton.setOnClickListener(v -> imageContainer.removeAllViews());
        ListView listView = binding.employeesListView;
        employees = getEmployees();
        ArrayAdapter<String> employeesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, employees);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(employeesAdapter);
        binding.ServiceCategory.setAdapter(setCategoriesSpinnerAdapter());
        binding.ServiceSubCategory.setAdapter(setSubCategoriesSpinnerAdapter());
        Button switchPageButton = binding.switchPageButton;
        Button galleryButton = binding.addImageButtonService;
        galleryButton.setOnClickListener(v -> openGallery());
        switchPageButton.setOnClickListener(v -> switchFormPages());
        Button submitBtn = binding.submitButtonService;
        submitBtn.setOnClickListener(v -> addService());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGES_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    addImageToContainer(imageUri);
                }
            } else {
                Uri imageUri = data.getData();
                addImageToContainer(imageUri);
            }
        }
    }

    private void addImageToContainer(Uri imageUri) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageURI(imageUri);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                200,
                200
        ));

        ImageButton removeButton = new ImageButton(getActivity());
        removeButton.setImageResource(android.R.drawable.ic_delete);
        removeButton.setBackgroundColor(Color.TRANSPARENT);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageContainer.removeView(imageView);
            }
        });

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(imageView);
        imageContainer.addView(layout);
    }

    private void addService(){
        String category = binding.ServiceCategory.getSelectedItem().toString();
        String subcategory = binding.ServiceSubCategory.getSelectedItem().toString();
        String name = binding.ServiceName.getText().toString();
        String description = binding.editTextServiceDescription.getText().toString();
        String specificity = binding.ServiceSpecificity.getText().toString();
        String reservationDeadline = binding.editTextReservationDeadline.getText().toString();
        String cancelationDeadline = binding.editTextCancellationDeadline.getText().toString();
        String price = binding.ServicePrice.getText().toString();
        String minHour = binding.ServiceMinDurationHours.getText().toString();
        String maxHour = binding.ServiceMaxDurationHours.getText().toString();
        String minMin = binding.ServiceMinDurationMinutes.getText().toString();
        String maxMin = binding.ServiceMaxDurationMinutes.getText().toString();
        boolean visible = binding.checkBoxODAvailable.isChecked();
        boolean isAvailableToBuy = binding.checkBoxBuyAvailable.isChecked();
        boolean confirmAutomatically = binding.radioAutomatically.isChecked();
        int priceInt = Integer.parseInt(price);
        int minHourInt = Integer.parseInt(minHour);
        int maxHourInt = Integer.parseInt(maxHour);
        int minMinInt = Integer.parseInt(minMin);
        int maxMinInt = Integer.parseInt(maxMin);
        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add("tip1");
        eventTypes.add("tip2");
        Service service = new Service(0L, name, description, category, subcategory, specificity, priceInt, minHourInt, minMinInt,
                maxHourInt, maxMinInt, "SR", reservationDeadline, cancelationDeadline, new ArrayList<Integer>(), eventTypes,
                new ArrayList<String>(), confirmAutomatically);
        service.setVisible(visible);
        service.setAvailableToBuy(isAvailableToBuy);
        CloudStoreUtil.insertService(service);
        FragmentTransition.to(ServicesListFragment.newInstance(), getActivity(),
                false, R.id.scroll_services_list, "falsh");
    }

    private void switchFormPages(){
        Button tv = binding.switchPageButton;
        if(currentPage == 0) {
            part1.setVisibility(View.GONE);
            part2.setVisibility(View.VISIBLE);
            part3.setVisibility(View.GONE);
            currentPage = 1;
        }
        else if(currentPage == 1){
            part1.setVisibility(View.GONE);
            part2.setVisibility(View.GONE);
            part3.setVisibility(View.VISIBLE);
            currentPage = 2;
        }
        else{
            part1.setVisibility(View.VISIBLE);
            part2.setVisibility(View.GONE);
            part3.setVisibility(View.GONE);
            currentPage = 0;
        }
        String text = "Page " + (currentPage+1) + "/3";
        tv.setText(text);
    }
    private ArrayList<String> getEmployees(){
        ArrayList<String> list = new ArrayList<>();
        list.add("rope");
        list.add("pera");
        list.add("jova");
        return list;
    }

    private ArrayAdapter<String> setCategoriesSpinnerAdapter(){
        categories = new ArrayList<>();
        categories.add("Category 1");
        categories.add("Category 2");
        categories.add("Category 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<String> setSubCategoriesSpinnerAdapter(){
        subCategories = new ArrayList<>();
        subCategories.add("Sub-Category 1");
        subCategories.add("Sub-Category 2");
        subCategories.add("Sub-Category 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, subCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
