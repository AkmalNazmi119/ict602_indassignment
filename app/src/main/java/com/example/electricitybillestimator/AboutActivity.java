package com.example.electricitybillestimator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    TextView textInfo, textLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textInfo = findViewById(R.id.textInfo);
        textLink = findViewById(R.id.textLink);

        textInfo.setText(
                "Name: Akmal Nazmi\n" +
                        "Student ID: 2023126793\n" +
                        "Course: ICT602 – Mobile Technology\n" +
                        "© 2025 Electricity Bill Estimator");

        textLink.setText("Visit GitHub Page");
        textLink.setOnClickListener(view -> {
            String url = "https://github.com/yourgithubusername/electricity-bill-estimator"; // replace with your actual URL
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }
}
