package com.example.smartdesk;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.smartdesk.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    public NavController navController;
    public boolean isFirst = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.container_fragment);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

}