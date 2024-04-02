package com.example.ma02mibu.fragments.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.EventListFragmentBinding;
import com.example.ma02mibu.databinding.FragmentEventFormBinding;
import com.example.ma02mibu.databinding.FragmentProductsFilterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFormFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentEventFormBinding binding;

    public EventFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFormFragment newInstance(String param1, String param2) {
        EventFormFragment fragment = new EventFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button suggestionButton = binding.suggestion;
        addToBackStack(getActivity(),"createEventPage");
        suggestionButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_suggestion, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });
        Button budgetButton = binding.budget;
        budgetButton.setOnClickListener(v -> {
            FragmentTransition.to(EventBudgetFragment.newInstance("",""), getActivity(),
                    true, R.id.frameLayout2, "addBudgetPage");
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinnerEventType = view.findViewById(R.id.spinner_event_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.event_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventType.setAdapter(adapter);
    }

    public void addToBackStack(FragmentActivity activity, String backStackTag) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Add the existing fragment's view to the back stack
        transaction.addToBackStack(backStackTag);

        transaction.commit();
    }
}