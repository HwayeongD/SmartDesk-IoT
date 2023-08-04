package com.example.smartdesk.ui.seat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartdesk.R;
import com.example.smartdesk.databinding.FragmentSeatBinding;

import java.util.ArrayList;

public class SeatFragment extends Fragment {
    private FragmentSeatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SeatViewModel seatViewModel =
                new ViewModelProvider(this).get(SeatViewModel.class);

        binding = FragmentSeatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView;
        RecyclerViewAdapter adapter;
        GridLayoutManager layoutManager;

        ArrayList<Item> list = new ArrayList<Item>() {{
            add(new Item("201", R.drawable.seat));
            add(new Item("202", R.drawable.seat));
            add(new Item("", R.drawable.seat_empty));
            add(new Item("204", R.drawable.seat));
            add(new Item("205", R.drawable.seat));
            add(new Item("206", R.drawable.seat));
            add(new Item("206", R.drawable.seat));
            add(new Item("206", R.drawable.seat));

        }};

        recyclerView = root.findViewById(R.id.grid_recyclerview);
        adapter = new RecyclerViewAdapter(getContext(), list);

        layoutManager = new GridLayoutManager(getContext(), 5);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int gridPosition = position % 3;
                switch (gridPosition) {
                    case 0:
                    case 1:
                    case 2:
                        return 1;
                    case 3:
                    case 4:
                        return 3;
                }
                return 0;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
