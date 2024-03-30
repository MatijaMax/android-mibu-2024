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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.NewProductBinding;
import com.example.ma02mibu.databinding.ServicesPageFragmentBinding;
import com.example.ma02mibu.fragments.HomeFragment;
import com.example.ma02mibu.fragments.services.ServicesListFragment;

public class NewProduct extends Fragment {
    NewProductBinding binding;
    private LinearLayout imageContainer;
    private static final int PICK_IMAGES_REQUEST = 1;
    public static NewProduct newInstance() {
        return new NewProduct();
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = NewProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button galleryButton = binding.addImageButton;
        galleryButton.setOnClickListener(v -> openGallery());
        imageContainer = binding.imageContainer;
        ImageButton removeButton = binding.removeImagesButton;
        removeButton.setOnClickListener(v -> imageContainer.removeAllViews());
        return root;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}