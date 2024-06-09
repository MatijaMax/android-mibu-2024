package com.example.ma02mibu.fragments.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentInfoMaxBinding;
import com.example.ma02mibu.model.ProductDAO;

public class InfoMaxFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_PARAM3 = "param3";

    private ProductDAO mProduct;
    private String ownerRefId;
    private FragmentInfoMaxBinding binding;

    private EditText nameEditText;
    private EditText ageEditText;
    private CheckBox invitedCheckBox;
    private CheckBox acceptedCheckBox;
    private EditText specialEditText;
    private RecyclerView recyclerView;

    private Boolean isFav;

    public interface OnFragmentClosedListener {
        void onFragmentClosed();
    }

    private OnFragmentClosedListener listener;

    public void setOnFragmentClosedListener(OnFragmentClosedListener listener) {
        this.listener = listener;
    }

    public static InfoMaxFragment newInstance(ProductDAO param1, String param2, boolean param3) {
        InfoMaxFragment fragment = new InfoMaxFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProduct = getArguments().getParcelable(ARG_PARAM1);
            ownerRefId = getArguments().getString(ARG_PARAM2);
            isFav = getArguments().getBoolean(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInfoMaxBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        nameEditText = view.findViewById(R.id.editTextName);
        ageEditText = view.findViewById(R.id.editTextAge);
        invitedCheckBox = view.findViewById(R.id.checkBoxInvited);
        acceptedCheckBox = view.findViewById(R.id.checkBoxAccepted);
        specialEditText = view.findViewById(R.id.editTextSpecial);
        recyclerView = view.findViewById(R.id.recyclerView);

        if(isFav){
            FragmentTransition.to(MaxInfoFragment.newInstance(mProduct, mProduct.getOwnerUuid(), isFav), getActivity(),
                    true, R.id.fav_info_layout, "INFOMAX");
        }
        else{
            FragmentTransition.to(MaxInfoFragment.newInstance(mProduct, mProduct.getOwnerUuid(), isFav), getActivity(),
                    true, R.id.scroll_products_list2, "INFOMAX");
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        notifyFragmentClosed();
    }

    private void notifyFragmentClosed() {
        if (listener != null) {
            listener.onFragmentClosed();
        }
    }
}

