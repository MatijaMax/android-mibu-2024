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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.EmployeeListAdapter;
import com.example.ma02mibu.adapters.adminsManagment.CategoryListAdapter;
import com.example.ma02mibu.databinding.NewProductBinding;
import com.example.ma02mibu.databinding.NewServiceBinding;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.Deadline;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EmployeeInService;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.Subcategory;
import com.example.ma02mibu.model.SubcategoryProposal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.UUID;


public class NewService extends Fragment {

    int currentPage;
    private static final int PICK_IMAGES_REQUEST = 1;
    private LinearLayout imageContainer;
    private ArrayList<String> categories;
    private Category currentCategory;
    private ArrayList<String> subCategories;
    private ArrayList<String> deadlineFormats;
    LinearLayout part1;
    LinearLayout part2;
    LinearLayout part3;
    ArrayList<EmployeeInService> employees = new ArrayList<>();
    ArrayList<Category> categoriesDB = new ArrayList<>();
    NewServiceBinding binding;
    private FirebaseAuth auth;
    private String ownerId;
    boolean newSubCatShow;
    public static NewService newInstance() {
        return new NewService();
    }
    //tutorijal za rad sa listview-om i biranje iz listview-a: https://www.youtube.com/watch?v=l3BCsSUZoNE
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ownerId = user.getUid();
        currentPage = 0;
        binding = NewServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        part1 = binding.layoutPart1;
        part2 = binding.layoutPart2;
        part3 = binding.layoutPart3;
        imageContainer = binding.imageContainerService;
        ImageButton removeButton = binding.removeImagesButton;
        removeButton.setOnClickListener(v -> imageContainer.removeAllViews());
        ImageButton newSubCat = binding.newSubCategory;
        newSubCat.setOnClickListener(v -> toggleNewSubCategory());
        setCategoriesSpinnerAdapter();
        setEventTypesAdapter();
        getEmployees(ownerId);
        binding.resDeadlineFormat.setAdapter(setDateFormatAdapter());
        binding.cancDeadlineFormat.setAdapter(setDateFormatAdapter());
        Button switchPageButton = binding.switchPageButton;
        Button galleryButton = binding.addImageButtonService;
        galleryButton.setOnClickListener(v -> openGallery());
        switchPageButton.setOnClickListener(v -> switchFormPages());
        Button submitBtn = binding.submitButtonService;
        submitBtn.setOnClickListener(v -> addService());
        setCategoryChangeListener();
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
        ListView listView = binding.employeesListView;
        int cnt = listView.getAdapter().getCount();
        ArrayList<EmployeeInService> checkedEmployees = new ArrayList<>();
        for(int i=0; i<cnt; i++){
            if(listView.isItemChecked(i))
                checkedEmployees.add(employees.get(i));
        }

        ListView listViewEventTypes = binding.eventTypesListService;
        cnt = listViewEventTypes.getAdapter().getCount();
        ArrayList<String> eventTypes = new ArrayList<>();
        for(int i=0; i<cnt; i++){
            if(listViewEventTypes.isItemChecked(i))
                eventTypes.add(listViewEventTypes.getItemAtPosition(i).toString());
        }

        binding.validation.setVisibility(View.GONE);
        String category = binding.ServiceCategory.getSelectedItem().toString();
        String subcategory = binding.ServiceSubCategory.getSelectedItem().toString();
        String name = binding.ServiceName.getText().toString();
        String description = binding.editTextServiceDescription.getText().toString();
        String specificity = binding.ServiceSpecificity.getText().toString();
        String reservationDeadlineNum = binding.editTextReservationDeadline.getText().toString();
        String cancelationDeadlineNum = binding.editTextCancellationDeadline.getText().toString();
        String reservationDeadlineFormat = binding.resDeadlineFormat.getSelectedItem().toString();
        String cancelationDeadlineFormat = binding.cancDeadlineFormat.getSelectedItem().toString();
        String price = binding.ServicePrice.getText().toString();
        String minHour = binding.ServiceMinDurationHours.getText().toString();
        String maxHour = binding.ServiceMaxDurationHours.getText().toString();
        String minMin = binding.ServiceMinDurationMinutes.getText().toString();
        String maxMin = binding.ServiceMaxDurationMinutes.getText().toString();
        String newSubCategory = binding.SubCategoryRecommendation.getText().toString();
        if(name.equals("") || description.equals("") || price.equals("") || reservationDeadlineNum.equals("")
        || cancelationDeadlineNum.equals("") || minHour.equals("") || maxHour.equals("") || minMin.equals("") || maxMin.equals("")){
            binding.validation.setVisibility(View.VISIBLE);
            return;
        }
        boolean visible = binding.checkBoxODAvailable.isChecked();
        boolean isAvailableToBuy = binding.checkBoxBuyAvailable.isChecked();
        boolean confirmAutomatically = binding.radioAutomatically.isChecked();
        int priceInt = Integer.parseInt(price);
        int minHourInt = Integer.parseInt(minHour);
        int maxHourInt = Integer.parseInt(maxHour);
        int minMinInt = Integer.parseInt(minMin);
        int maxMinInt = Integer.parseInt(maxMin);
        Deadline resDeadline = new Deadline(reservationDeadlineFormat, Integer.parseInt(reservationDeadlineNum));
        Deadline cancDeadline = new Deadline(cancelationDeadlineFormat, Integer.parseInt(cancelationDeadlineNum));
        Service service = new Service(0L, name, description, category, subcategory, specificity, priceInt, minHourInt, minMinInt,
                maxHourInt, maxMinInt, "SR", resDeadline, cancDeadline, new ArrayList<Integer>(), eventTypes,
                checkedEmployees, confirmAutomatically);
        service.setVisible(visible);
        service.setAvailableToBuy(isAvailableToBuy);
        service.setOwnerUuid(ownerId);
        boolean saveNewSubCategory = false;
        SubcategoryProposal subcategoryProposal = new SubcategoryProposal();
        if(!newSubCategory.isEmpty()){
            service.setPending(true);
            service.setSubCategory(newSubCategory);
            Subcategory proposedSubCategory = new Subcategory(currentCategory.getDocumentRefId(), newSubCategory, "opis", Subcategory.SUBCATEGORYTYPE.USLUGA);
            subcategoryProposal = new SubcategoryProposal(proposedSubCategory, "");
            saveNewSubCategory = true;
        }
        CloudStoreUtil.insertService(service, saveNewSubCategory, subcategoryProposal);
        FragmentTransition.to(ServicesListFragment.newInstance(), getActivity(),
                false, R.id.scroll_services_list, "falsh");
    }

    private void toggleNewSubCategory(){
        TextView subCatText = binding.SubCategoryRecommendation;
        if(newSubCatShow)
            subCatText.setVisibility(View.VISIBLE);
        else
            subCatText.setVisibility(View.GONE);
        newSubCatShow = !newSubCatShow;
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
    private void getEmployees(String ownerRefId){
        CloudStoreUtil.getEmployeesList(ownerRefId, new CloudStoreUtil.EmployeesListCallback() {
            @Override
            public void onSuccess(ArrayList<Employee> itemList) {
                // Handle the retrieved list of items (e.g., display them in UI)
                ArrayList<Employee> employeesDB = new ArrayList<>(itemList);
                for(Employee e: employeesDB){
                    employees.add(new EmployeeInService(e.getEmail(), e.getFirstName(), e.getLastName()));
                }

                ListView listView = binding.employeesListView;
                ArrayList<String> employeesInList = new ArrayList<>();
                for (EmployeeInService e: employees)
                    employeesInList.add(e.getFirstName() + " " + e.getLastName());
                ArrayAdapter<String> employeesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, employeesInList);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(employeesAdapter);

            }

            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error fetching documents: " + e.getMessage());
            }
         });
    }

    private void setCategoriesSpinnerAdapter(){
        CloudStoreUtil.selectCategories(result -> {
            categoriesDB = result;
            categories = new ArrayList<>();
            for(Category c: categoriesDB)
                categories.add(c.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.ServiceCategory.setAdapter(adapter);
            currentCategory = categoriesDB.get(0);
            CloudStoreUtil.selectSubcategoriesFromCategory(categoriesDB.get(0).getDocumentRefId(), resultSC -> {
                ArrayList<Subcategory> subcategoriesDB = resultSC;
                subCategories = new ArrayList<>();
                for(Subcategory s: subcategoriesDB)
                    subCategories.add(s.getName());
                ArrayAdapter<String> adapterSC = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, subCategories);
                adapterSC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.ServiceSubCategory.setAdapter(adapterSC);
            });
        });
    }
    private void setCategoryChangeListener(){
        binding.ServiceCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CloudStoreUtil.selectSubcategoriesFromCategory(categoriesDB.get(position).getDocumentRefId(), resultSC -> {
                    ArrayList<Subcategory> subcategoriesDB = resultSC;
                    subCategories = new ArrayList<>();
                    for(Subcategory s: subcategoriesDB)
                        subCategories.add(s.getName());
                    ArrayAdapter<String> adapterSC = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, subCategories);
                    adapterSC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.ServiceSubCategory.setAdapter(adapterSC);
                    currentCategory = categoriesDB.get(position);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private ArrayAdapter<String> setDateFormatAdapter(){
        deadlineFormats = new ArrayList<>();
        deadlineFormats.add("days");
        deadlineFormats.add("months");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, deadlineFormats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void setEventTypesAdapter(){
        ListView listView = binding.eventTypesListService;
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
