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

import com.example.ma02mibu.databinding.NewProductBinding;
import com.example.ma02mibu.databinding.NewServiceBinding;

import java.util.ArrayList;


public class NewService extends Fragment {

    int currentPage;
    private static final int PICK_IMAGES_REQUEST = 1;
    private LinearLayout imageContainer;
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
        Button switchPageButton = binding.switchPageButton;
        Button galleryButton = binding.addImageButtonService;
        galleryButton.setOnClickListener(v -> openGallery());
        switchPageButton.setOnClickListener(v -> switchFormPages());
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

    private void switchFormPages(){
        TextView tv = binding.switchPageButton;
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
        String text = "Page" + currentPage + "/3";
        tv.setText(text);
    }
    private ArrayList<String> getEmployees(){
        ArrayList<String> list = new ArrayList<>();
        list.add("rope");
        list.add("pera");
        list.add("jova");
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
