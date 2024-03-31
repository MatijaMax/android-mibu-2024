package com.example.ma02mibu.fragments.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.AuthenticationActivity;
import com.example.ma02mibu.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpPUPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpPUPFragment extends Fragment {
    public SignUpPUPFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpPUPFragment.
     */
    public static SignUpPUPFragment newInstance() {
        SignUpPUPFragment fragment = new SignUpPUPFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_p_u_p, container, false);
        Button btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(SignUpPUP2Fragment.newInstance("",""), getActivity(), true, R.id.authenticationFragmentContainer);
            }
        });

        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        return view;
    }

    private void chooseImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivity(i);
    }
}