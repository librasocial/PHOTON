package com.ssf.homevisit.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ssf.homevisit.R;
import com.ssf.homevisit.fragment.SociaEcoSurveyFragment;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent i=new Intent(getBaseContext(), SociaEcoSurveyFragment.class);
        startActivity(i);
    }
}