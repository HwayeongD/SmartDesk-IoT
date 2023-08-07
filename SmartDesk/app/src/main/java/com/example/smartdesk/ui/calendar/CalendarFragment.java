package com.example.smartdesk.ui.calendar;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.R;
import com.example.smartdesk.databinding.FragmentCalendarBinding;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarFragment extends Fragment {
    private FragmentCalendarBinding binding;
    TextView month_year;
    LocalDate selectedDate;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        month_year = root.findViewById(R.id.month_year);
        ImageButton pre_month  = root.findViewById(R.id.pre_month);
        ImageButton next_month = root.findViewById(R.id.next_month);

        // 현재 날짜
        selectedDate = LocalDate.now();

        // 화면 설정
        setMonthView();

        // 버튼 이벤트
        pre_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    // 날짜 타입 설정
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM월");

        return date.format(formatter);
    };

    private void setMonthView() {
        month_year.setText(monthYearFromDate(selectedDate));
    }

}
