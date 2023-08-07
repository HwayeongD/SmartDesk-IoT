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
            add(new Item("201", R.drawable.seat, "202", R.drawable.seat, "203", R.drawable.seat, "204", R.drawable.seat, "205", R.drawable.seat, "206", R.drawable.seat));
            add(new Item("207", R.drawable.seat, "208", R.drawable.seat, "209", R.drawable.seat, "210", R.drawable.seat, "211", R.drawable.seat, "212", R.drawable.seat));
            add(new Item("213", R.drawable.seat, "214", R.drawable.seat, "215", R.drawable.seat, "216", R.drawable.seat, "217", R.drawable.seat, "218", R.drawable.seat));
            add(new Item("219", R.drawable.seat, "220", R.drawable.seat, "221", R.drawable.seat, "222", R.drawable.seat, "223", R.drawable.seat, "224", R.drawable.seat));
            add(new Item("225", R.drawable.seat, "226", R.drawable.seat, "227", R.drawable.seat, "228", R.drawable.seat, "229", R.drawable.seat, "230", R.drawable.seat));
            add(new Item("231", R.drawable.seat, "232", R.drawable.seat, "233", R.drawable.seat, "234", R.drawable.seat, "235", R.drawable.seat, "236", R.drawable.seat));
            add(new Item("237", R.drawable.seat, "238", R.drawable.seat, "239", R.drawable.seat, "240", R.drawable.seat, "241", R.drawable.seat, "242", R.drawable.seat));


        }};

        recyclerView = root.findViewById(R.id.grid_recyclerview);
        adapter = new RecyclerViewAdapter(getContext(), list);

        layoutManager = new GridLayoutManager(getContext(), 2);

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
