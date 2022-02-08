package com.aysu.traveldaily.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aysu.traveldaily.R;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void sıgnupp(View view){
        Intent ıntent=new Intent(this, MainActivity4.class);
        startActivity(ıntent);
    }

}