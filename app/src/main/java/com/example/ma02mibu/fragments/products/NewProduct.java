package com.example.ma02mibu.fragments.products;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.NewProductBinding;
import com.example.ma02mibu.databinding.ServicesPageFragmentBinding;
import com.example.ma02mibu.fragments.HomeFragment;
import com.example.ma02mibu.fragments.services.ServicesListFragment;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class NewProduct extends Fragment {
    NewProductBinding binding;
    boolean newSubCatShow;
    private ArrayList<String> categories;
    private ArrayList<String> subCategories;
    private LinearLayout imageContainer;
    private static final int PICK_IMAGES_REQUEST = 1;
    public static NewProduct newInstance() {
        return new NewProduct();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newSubCatShow = false;
        binding = NewProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button galleryButton = binding.addImageButton;
        galleryButton.setOnClickListener(v -> openGallery());
        imageContainer = binding.imageContainer;
        ImageButton removeButton = binding.removeImagesButton;
        removeButton.setOnClickListener(v -> imageContainer.removeAllViews());
        ImageButton newSubCat = binding.newSubCategory;
        newSubCat.setOnClickListener(v -> toggleNewSubCategory());
        Button submitBtn = binding.submitButton;
        binding.ProductCategory.setAdapter(setCategoriesSpinnerAdapter());
        binding.ProductSubCategory.setAdapter(setSubCategoriesSpinnerAdapter());
        submitBtn.setOnClickListener(v -> addProduct());
        return root;
    }
    private void toggleNewSubCategory(){
        TextView subCatText = binding.SubCategoryRecommendation;
        if(newSubCatShow)
            subCatText.setVisibility(View.VISIBLE);
        else
            subCatText.setVisibility(View.GONE);
        newSubCatShow = !newSubCatShow;
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
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    private void addProduct(){
        String category = binding.ProductCategory.getSelectedItem().toString();
        String subcategory = binding.ProductSubCategory.getSelectedItem().toString();
        String name = binding.ProductName.getText().toString();
        String description = binding.editTextProductDescription.getText().toString();
        String price = binding.ProductPrice.getText().toString();
        boolean visible = binding.checkBoxODAvailable.isChecked();
        boolean isAvailableToBuy = binding.checkBoxBuyAvailable.isChecked();
        int priceInt = Integer.parseInt(price);
        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add("tip1");
        eventTypes.add("tip2");
        Product product = new Product(0L, name, description, category, subcategory, priceInt,
                new ArrayList<Integer>(), eventTypes, 0);
        product.setVisible(visible);
        product.setAvailableToBuy(isAvailableToBuy);
        CloudStoreUtil.insertProduct(product);
        FragmentTransition.to(ProductsListFragment.newInstance(), getActivity(),
                false, R.id.scroll_products_list, "falsh");
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