package com.example.smartdesk.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {
    private FragmentCalendarBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        RecyclerView recyclerView = root.findViewById(R.id.month_recycler);
//        List<String> dayList = new ArrayList<>(); // Replace with your actual data
//        CalendarAdapter adapter = new CalendarAdapter(getContext(), dayList);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);




        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
