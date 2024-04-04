package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.adminsManagment.SubcategoryRequestListAdapter;
import com.example.ma02mibu.model.SubCategory;
import com.example.ma02mibu.model.SubCategoryRequest;

import java.util.ArrayList;

public class SubcategoryRequestReviewTabFragment extends Fragment {
    private SubcategoryRequestListAdapter subcategoryRequestListAdapter;
    private ArrayList<SubCategoryRequest> subCategoryRequests = new ArrayList<>();
    public SubcategoryRequestReviewTabFragment() { }

    public static SubcategoryRequestReviewTabFragment newInstance() {
        SubcategoryRequestReviewTabFragment fragment = new SubcategoryRequestReviewTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSubCategoryRequests();
        subcategoryRequestListAdapter = new SubcategoryRequestListAdapter(getActivity(), subCategoryRequests);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subcategory_request_review_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView subcategoryRequestListView = view.findViewById(R.id.subCategoriesRequestsListView);
        subcategoryRequestListView.setAdapter(subcategoryRequestListAdapter);
        subcategoryRequestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransition.to(SubcategoryRequestReviewEditFragment.newInstance(subcategoryRequestListAdapter.getItem(position)),
                        getActivity(), true, R.id.categoryManagementContainer, "subcategoryRequestManagement");
            }
        });
    }

    private void createSubCategoryRequests(){
        subCategoryRequests.add(new SubCategoryRequest(1L, 1L, "Ime podkategorije 1", "Opis podkategorije 1", SubCategory.SUBCATEGORYTYPE.PROIZVOD, 1L));
        subCategoryRequests.add(new SubCategoryRequest(2L, 2L, "Ime podkategorije 2", "Opis podkategorije 2", SubCategory.SUBCATEGORYTYPE.USLUGA, 1L));
        subCategoryRequests.add(new SubCategoryRequest(3L, 2L, "Ime podkategorije 3", "Opis podkategorije 3", SubCategory.SUBCATEGORYTYPE.USLUGA, 3L));
        subCategoryRequests.add(new SubCategoryRequest(4L, 2L, "Ime podkategorije 4", "Opis podkategorije 4", SubCategory.SUBCATEGORYTYPE.PROIZVOD, 2L));
        subCategoryRequests.add(new SubCategoryRequest(5L, 4L, "Ime podkategorije 5", "Opis podkategorije 5", SubCategory.SUBCATEGORYTYPE.PROIZVOD, 1L));
        subCategoryRequests.add(new SubCategoryRequest(6L, 1L, "Ime podkategorije 6", "Opis podkategorije 6", SubCategory.SUBCATEGORYTYPE.USLUGA, 6L));
        subCategoryRequests.add(new SubCategoryRequest(7L, 4L, "Ime podkategorije 7", "Opis podkategorije 7", SubCategory.SUBCATEGORYTYPE.USLUGA, 9L));
        subCategoryRequests.add(new SubCategoryRequest(8L, 1L, "Ime podkategorije 8", "Opis podkategorije 8", SubCategory.SUBCATEGORYTYPE.USLUGA, 6L));
        subCategoryRequests.add(new SubCategoryRequest(9L, 1L, "Ime podkategorije 9", "Opis podkategorije 9", SubCategory.SUBCATEGORYTYPE.USLUGA, 4L));
    }
}