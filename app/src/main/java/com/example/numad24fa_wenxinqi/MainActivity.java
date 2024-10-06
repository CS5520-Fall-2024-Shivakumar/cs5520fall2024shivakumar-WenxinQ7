package com.example.numad24fa_wenxinqi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the "About Me" button
        Button aboutMeButton = findViewById(R.id.buttonAboutMe);
        aboutMeButton.setOnClickListener(v -> {
            // Display the toast with your name and email
            Toast.makeText(MainActivity.this, "Wenxin Qi\nqi.wenx@northeastern.edu", Toast.LENGTH_SHORT).show();
        });

        Button quicCalcButton = findViewById(R.id.buttonQuicCalc);
        quicCalcButton.setOnClickListener(v -> {
            // Start the calculator activity
            Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(intent);
        });
    }
}