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
import android.widget.AdapterView;
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
import com.example.ma02mibu.adapters.adminsManagment.EventTypeListAdapter;
import com.example.ma02mibu.databinding.NewProductBinding;
import com.example.ma02mibu.databinding.ServicesPageFragmentBinding;
import com.example.ma02mibu.fragments.HomeFragment;
import com.example.ma02mibu.fragments.services.ServicesListFragment;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.EmployeeInService;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Subcategory;
import com.example.ma02mibu.model.SubcategoryProposal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.UUID;

public class NewProduct extends Fragment {
    NewProductBinding binding;
    boolean newSubCatShow;
    private ArrayList<String> categories;
    private ArrayList<String> subCategories;
    private LinearLayout imageContainer;
    private Category currentCategory;
    ArrayList<Category> categoriesDB = new ArrayList<>();
    private FirebaseAuth auth;
    private String ownerId;
    private static final int PICK_IMAGES_REQUEST = 1;
    public static NewProduct newInstance() {
        return new NewProduct();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ownerId = user.getUid();
        newSubCatShow = false;
        binding = NewProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setCategoriesSpinnerAdapter();
        Button galleryButton = binding.addImageButton;
        galleryButton.setOnClickListener(v -> openGallery());
        imageContainer = binding.imageContainer;
        ImageButton removeButton = binding.removeImagesButton;
        removeButton.setOnClickListener(v -> imageContainer.removeAllViews());
        ImageButton newSubCat = binding.newSubCategory;
        newSubCat.setOnClickListener(v -> toggleNewSubCategory());
        Button submitBtn = binding.submitButton;
        setEventTypesAdapter();
        submitBtn.setOnClickListener(v -> addProduct());
        setCategoryChangeListener();
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
        binding.validation.setVisibility(View.GONE);
        ListView listView = binding.eventTypesList;
        int cnt = listView.getAdapter().getCount();
        ArrayList<String> eventTypes = new ArrayList<>();
        for(int i=0; i<cnt; i++){
            if(listView.isItemChecked(i))
                eventTypes.add(listView.getItemAtPosition(i).toString());
        }
        String category = binding.ProductCategory.getSelectedItem().toString();
        String subcategory = binding.ProductSubCategory.getSelectedItem().toString();
        String name = binding.ProductName.getText().toString();
        String description = binding.editTextProductDescription.getText().toString();
        String price = binding.ProductPrice.getText().toString();
        String newSubCategory = binding.SubCategoryRecommendation.getText().toString();
        if(name.equals("") || description.equals("") || price.equals("")){
            binding.validation.setVisibility(View.VISIBLE);
            return;
        }
        boolean visible = binding.checkBoxODAvailable.isChecked();
        boolean isAvailableToBuy = binding.checkBoxBuyAvailable.isChecked();
        int priceInt = Integer.parseInt(price);

        long id = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        Product product = new Product(id, name, description, category, subcategory, priceInt,
                new ArrayList<Integer>(), eventTypes, 0);
        product.setVisible(visible);
        product.setAvailableToBuy(isAvailableToBuy);
        product.setOwnerUuid(ownerId);
        boolean saveNewSubCategory = false;
        SubcategoryProposal subcategoryProposal = new SubcategoryProposal();
        if(!newSubCategory.isEmpty()){
            product.setPending(true);
            product.setSubCategory(newSubCategory);
            Subcategory proposedSubCategory = new Subcategory(currentCategory.getDocumentRefId(), newSubCategory, "opis", Subcategory.SUBCATEGORYTYPE.PROIZVOD);
            subcategoryProposal = new SubcategoryProposal(proposedSubCategory, "");
            saveNewSubCategory = true;
        }
        CloudStoreUtil.insertProduct(product, saveNewSubCategory, subcategoryProposal);
        FragmentTransition.to(ProductsListFragment.newInstance(), getActivity(),
                false, R.id.scroll_products_list, "falsh");
    }
    private void setCategoriesSpinnerAdapter(){
        CloudStoreUtil.selectCategories(result -> {
            categoriesDB = result;
            categories = new ArrayList<>();
            for(Category c: categoriesDB)
                categories.add(c.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.ProductCategory.setAdapter(adapter);
            currentCategory = categoriesDB.get(0);
            CloudStoreUtil.selectSubcategoriesFromCategory(categoriesDB.get(0).getDocumentRefId(), resultSC -> {
                ArrayList<Subcategory> subcategoriesDB = resultSC;
                subCategories = new ArrayList<>();
                for(Subcategory s: subcategoriesDB)
                    subCategories.add(s.getName());
                ArrayAdapter<String> adapterSC = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, subCategories);
                adapterSC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.ProductSubCategory.setAdapter(adapterSC);
            });
        });

    }
    private void setCategoryChangeListener(){
        binding.ProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CloudStoreUtil.selectSubcategoriesFromCategory(categoriesDB.get(position).getDocumentRefId(), resultSC -> {
                    ArrayList<Subcategory> subcategoriesDB = resultSC;
                    subCategories = new ArrayList<>();
                    for(Subcategory s: subcategoriesDB)
                        subCategories.add(s.getName());
                    ArrayAdapter<String> adapterSC = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, subCategories);
                    adapterSC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.ProductSubCategory.setAdapter(adapterSC);
                    currentCategory = categoriesDB.get(position);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setEventTypesAdapter(){
        ListView listView = binding.eventTypesList;
        CloudStoreUtil.selectEventTypes(result -> {
            ArrayList<String> eventTypes = new ArrayList<>();
            for(EventType e: result){
                if(e.getStatus() == EventType.EVENTTYPESTATUS.ACTIVE)
                    eventTypes.add(e.getName());
            }
            ArrayAdapter<String> eventTypesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, eventTypes);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setAdapter(eventTypesAdapter);
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}