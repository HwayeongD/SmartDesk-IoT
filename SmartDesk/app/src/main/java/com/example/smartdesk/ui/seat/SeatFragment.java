package com.example.smartdesk.ui.seat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.R;
import com.example.smartdesk.databinding.FragmentSeatBinding;
import com.example.smartdesk.ui.dialog.ChangePasswordDialog;

public class SeatFragment extends Fragment {
    private FragmentSeatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SeatViewModel seatViewModel =
                new ViewModelProvider(this).get(SeatViewModel.class);

        binding = FragmentSeatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner floor_spinner = root.findViewById(R.id.floorSpinner);

//        floor_spinner.setOnClickListener(new View.OnClickListener() {
//
//        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
