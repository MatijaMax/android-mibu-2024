package com.example.ma02mibu.fragments.services;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.adapters.ServiceListAdapter;
import com.example.ma02mibu.databinding.FragmentServicesListBinding;
import com.example.ma02mibu.fragments.employees.EmployeePersonalWorkCalendarFragment;
import com.example.ma02mibu.fragments.products.NewProduct;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.WorkSchedule;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ServicesListFragment extends ListFragment {

    private FragmentServicesListBinding binding;

    private ArrayList<String> categories;
    private ArrayList<String> subCategories;
    private ArrayList<Service> mServices;
    private ArrayList<Service> mServicesBackup;
    private ServiceListAdapter adapter;
    boolean showHeading = true;
    private static final String ARG_PARAM = "param";
    private FirebaseAuth auth;
    private String userId;
    private boolean isOwner = false;
    public static ServicesListFragment newInstance(){
        ServicesListFragment fragment = new ServicesListFragment();
        return fragment;
    }

    public static ServicesListFragment newInstance(ArrayList<Service> services) {
        ServicesListFragment fragment = new ServicesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, services);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }
        //getOwner(userId);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mServices = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new ServiceListAdapter(getActivity(), mServices, getActivity(), false, null, isOwner);
            setListAdapter(adapter);
            showHeading = false;
        }
        else {
            CloudStoreUtil.selectServices(new CloudStoreUtil.ServiceCallback() {
                @Override
                public void onCallbackService(ArrayList<Service> retrievedServices) {
                    if (retrievedServices != null) {
                        mServices = retrievedServices;
                    } else {
                        mServices = new ArrayList<>();
                    }
                    CloudStoreUtil.getOwner(userId, new CloudStoreUtil.OwnerCallback() {
                        @Override
                        public void onSuccess(Owner myItem) {
                            isOwner = true;
                            mServices.removeIf(s -> !s.getOwnerUuid().equals(userId));
                            mServices.removeIf(s -> s.isPending());
                            mServicesBackup = new ArrayList<>(mServices);
                            adapter = new ServiceListAdapter(getActivity(), mServices, getActivity(), false, null, isOwner);
                            setListAdapter(adapter);
                        }
                        @Override
                        public void onFailure(Exception e) {
                            isOwner = false;
                            getEmployeesServices();
                            adapter = new ServiceListAdapter(getActivity(), mServices, getActivity(), false, null, isOwner);
                            setListAdapter(adapter);
                            binding.newServiceButton.setVisibility(View.GONE);
                        }
                    });

                }
            });
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = FragmentServicesListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if(!showHeading){
            binding.linear1Services.setVisibility(View.GONE);
        }
        Button searchButton = binding.searchButtonServices;
        searchButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.service_search_dialog, null);
            bottomSheetDialog.setContentView(dialogView);
            Button submitSearchButton = bottomSheetDialog.findViewById(R.id.search_button_service);
            submitSearchButton.setOnClickListener(f -> {
                EditText nameSearch = bottomSheetDialog.findViewById(R.id.service_name_search);
                EditText employeeSearch = bottomSheetDialog.findViewById(R.id.employee_service_search);
                EditText minPriceSearch = bottomSheetDialog.findViewById(R.id.min_price_service);
                EditText maxPriceSearch = bottomSheetDialog.findViewById(R.id.max_price_service);
                EditText eventTypeSearch = bottomSheetDialog.findViewById(R.id.event_type_search_service);
                EditText category = bottomSheetDialog.findViewById(R.id.service_category_search);
                EditText subCategory = bottomSheetDialog.findViewById(R.id.service_subcategory_search);
                CheckBox availableToBuySearch = bottomSheetDialog.findViewById(R.id.available_service_search);
                boolean availableToBuy = availableToBuySearch.isChecked();
                String name = nameSearch.getText().toString();
                String employee = employeeSearch.getText().toString();
                String eventType = eventTypeSearch.getText().toString();
                Integer minPrice = Integer.parseInt(minPriceSearch.getText().toString().isEmpty() ? "0" : minPriceSearch.getText().toString());
                Integer maxPrice = Integer.parseInt(maxPriceSearch.getText().toString().isEmpty() ? "0" : maxPriceSearch.getText().toString());
                searchService(name, employee, minPrice, maxPrice, eventType, availableToBuy, category.getText().toString(), subCategory.getText().toString());
                bottomSheetDialog.dismiss();
            });
            Button resetBtn = bottomSheetDialog.findViewById(R.id.reset_search_service);
            resetBtn.setOnClickListener(f -> {
                resetServices();
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.show();
        });

        Button newServiceButton = binding.newServiceButton;
        newServiceButton.setOnClickListener(v -> {
            FragmentTransition.to(NewService.newInstance(), getActivity(),
                    true, R.id.scroll_services_list, "newServicePage");
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void searchService(String name, String employeeName, Integer minPrice, Integer maxPrice, String eventType, boolean availableToBuy, String category, String subCategory){
        mServices = new ArrayList<>(mServicesBackup);
        if (!name.isEmpty())
            mServices.removeIf(s -> !s.getName().toLowerCase().contains(name.toLowerCase()));
        if (!category.isEmpty())
            mServices.removeIf(s -> !s.getCategory().toLowerCase().contains(category.toLowerCase()));
        if (!subCategory.isEmpty())
            mServices.removeIf(s -> !s.getSubCategory().toLowerCase().contains(subCategory.toLowerCase()));
        if (!employeeName.isEmpty())
            mServices.removeIf(s -> !s.containsPerson(employeeName));
        if(minPrice != 0)
            mServices.removeIf(s -> minPrice > s.getPriceByHour());
        if(maxPrice != 0)
            mServices.removeIf(s -> maxPrice < s.getPriceByHour());
        if(!eventType.isEmpty())
            mServices.removeIf(s -> !s.containsEventType(eventType));
        if(availableToBuy)
            mServices.removeIf(s -> !s.isAvailableToBuy());
        adapter = new ServiceListAdapter(getActivity(), mServices, getActivity(), false, null, isOwner);
        setListAdapter(adapter);
    }
    private void resetServices(){
        mServices = new ArrayList<>(mServicesBackup);
        adapter = new ServiceListAdapter(getActivity(), mServices, getActivity(), false, null, isOwner);
        setListAdapter(adapter);
    }
    private void getEmployeesServices(){
        CloudStoreUtil.getEmployee(userId, new CloudStoreUtil.EmployeeCallback() {
            @Override
            public void onSuccess(Employee myItem) {
                mServices.removeIf(s -> !s.getOwnerUuid().equals(myItem.getOwnerRefId()));
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
