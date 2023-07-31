package com.example.smartdesk.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView scheduleTime = binding.todayScheduleTime;
        final TextView scheduleContent = binding.todayScheduleContent;

        homeViewModel.getTextTime().observe(getViewLifecycleOwner(), scheduleTime::setText);
        homeViewModel.getTextContent().observe(getViewLifecycleOwner(), scheduleContent::setText);

        setEmpInfo();

        return root;
    }
    
    // 응답받은 사원정보를 화면에 출력
    public void setEmpInfo() {

    }

    public void btnScheduleClick(View view) {
        Toast.makeText(this.getContext(), "schedule btn is clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}