package com.example.betaforall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.betaforall.databinding.ActivityProgramBinding;

public class Program extends AppCompatActivity {

    private ActivityProgramBinding binding;
    private Button SaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SaveButton = findViewById(R.id.SaveButton);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Настройка цветов для элементов меню программно
        navView.setItemIconTintList(getResources().getColorStateList(R.color.nav_item_icon_tint));
        navView.setItemTextColor(getResources().getColorStateList(R.color.nav_item_text_color));

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.blankFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_program);
        NavigationUI.setupWithNavController(binding.navView, navController);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Program.this, Save.class);
                startActivity(intent);
            }
        });
    }
}
