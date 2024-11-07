package com.example.numad24fa_wenxinqi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TextView textView = findViewById(R.id.textViewAboutMe);
        textView.setText("Wenxin Qi\nqi.wenx@northeastern.edu");
    }
}